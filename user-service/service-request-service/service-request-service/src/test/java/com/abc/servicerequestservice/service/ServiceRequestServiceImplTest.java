package com.abc.servicerequestservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.abc.servicerequestservice.dto.CategoryResponse;
import com.abc.servicerequestservice.dto.ServiceRequestDetailResponse;
import com.abc.servicerequestservice.dto.ServiceRequestRequest;
import com.abc.servicerequestservice.dto.ServiceRequestUpdateDTO;
import com.abc.servicerequestservice.entity.ServiceRequest;
import com.abc.servicerequestservice.enums.RequestStatus;
import com.abc.servicerequestservice.exception.InvalidRequestStatusException;
import com.abc.servicerequestservice.exception.ServiceRequestNotFoundException;
import com.abc.servicerequestservice.feign.CategoryClient;
import com.abc.servicerequestservice.feign.AssignmentClient;
import com.abc.servicerequestservice.feign.NotificationClient;
import com.abc.servicerequestservice.feign.UserClient;
import com.abc.servicerequestservice.repository.ServiceRequestRepository;

class ServiceRequestServiceImplTest {

    private ServiceRequestRepository repository;
    private CategoryClient categoryClient;
    private ModelMapper mapper;
    private ApplicationEventPublisher eventPublisher;
    private AssignmentClient assignmentClient;
    private NotificationClient notificationClient;
    private UserClient userClient;
    private ServiceRequestServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(ServiceRequestRepository.class);
        categoryClient = mock(CategoryClient.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        assignmentClient = mock(AssignmentClient.class);
        notificationClient = mock(NotificationClient.class);
        userClient = mock(UserClient.class);
        mapper = new ModelMapper();

        service = new ServiceRequestServiceImpl(
                repository,
                mapper,
                categoryClient,
                eventPublisher,
                assignmentClient,
                notificationClient,
                userClient
        );
    }

    @Test
    void testCreateRequest() {
        ServiceRequestRequest request = new ServiceRequestRequest(101L, 1L, "Pipe Repair", "Hyderabad", LocalDateTime.now());

        ServiceRequest saved = new ServiceRequest();
        saved.setId(1L);
        saved.setCustomerId(101L);
        saved.setCategoryId(1L);
        saved.setServiceType("Pipe Repair");
        saved.setAddress("Hyderabad");
        saved.setScheduledTime(request.getScheduledTime());
        saved.setStatus(RequestStatus.CREATED);

        when(repository.save(any(ServiceRequest.class))).thenReturn(saved);

        ServiceRequestDetailResponse response = service.createRequest(request);

        assertEquals(RequestStatus.CREATED, response.getStatus());
        assertEquals(1L, response.getRequestId());
        verify(eventPublisher, times(1)).publishEvent(any(ServiceRequest.class));
    }

    @Test
    void testGetRequestsByCustomer() {
        ServiceRequest request = new ServiceRequest(1L, 101L, 1L, "Pipe Repair", RequestStatus.CREATED, "Hyderabad", LocalDateTime.now());
        Page<ServiceRequest> page = new PageImpl<>(Collections.singletonList(request));

        when(repository.findByCustomerId(eq(101L), any(Pageable.class))).thenReturn(page);

        Page<ServiceRequestDetailResponse> responsePage = service.getRequestsByCustomer(101L, PageRequest.of(0, 5));

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(101L, responsePage.getContent().get(0).getCustomerId());
    }

    @Test
    void testUpdateRequest() {
        ServiceRequestUpdateDTO updateDTO = new ServiceRequestUpdateDTO("Pipe Repair Updated", "Hyderabad Updated", LocalDateTime.now());
        ServiceRequest request = new ServiceRequest(1L, 101L, 1L, "Pipe Repair", RequestStatus.CREATED, "Hyderabad", LocalDateTime.now());

        when(repository.findById(1L)).thenReturn(Optional.of(request));
        when(repository.save(any(ServiceRequest.class))).thenAnswer(inv -> inv.getArgument(0));

        ServiceRequestDetailResponse response = service.updateRequest(1L, updateDTO);

        assertEquals("Pipe Repair Updated", response.getServiceType());
        assertEquals("Hyderabad Updated", response.getAddress());
        verify(repository, times(1)).save(any(ServiceRequest.class));
    }

    @Test
    void testCancelCreatedRequest_NoTechnician() {
        ServiceRequest request = new ServiceRequest();
        request.setId(1L);
        request.setStatus(RequestStatus.CREATED);

        when(repository.findById(1L)).thenReturn(Optional.of(request));
        when(repository.save(any(ServiceRequest.class))).thenAnswer(inv -> inv.getArgument(0));

        service.cancelRequest(1L);

        assertEquals(RequestStatus.CANCELLED, request.getStatus());
        verify(assignmentClient, never()).getAllAssignments(anyString());
        verify(notificationClient, never()).sendNotification(any());
    }

    @Test
    void testCancelAssignedRequest_WithTechnician_SendsNotification() {
        ServiceRequest request = new ServiceRequest();
        request.setId(1L);
        request.setStatus(RequestStatus.ASSIGNED);

        when(repository.findById(1L)).thenReturn(Optional.of(request));
        when(repository.save(any(ServiceRequest.class))).thenAnswer(inv -> inv.getArgument(0));

        AssignmentClient.AssignmentDto assignment = new AssignmentClient.AssignmentDto();
        assignment.setServiceRequestId(1L);
        assignment.setTechnicianId(202L);

        when(assignmentClient.getAllAssignments("ADMIN"))
            .thenReturn(ResponseEntity.ok(Collections.singletonList(assignment)));

        service.cancelRequest(1L);

        assertEquals(RequestStatus.CANCELLED, request.getStatus());
        verify(notificationClient, times(1)).sendNotification(any(NotificationClient.NotificationRequest.class));
    }

    @Test
    void testCancelCompletedRequest_ThrowsException() {
        ServiceRequest request = new ServiceRequest();
        request.setId(1L);
        request.setStatus(RequestStatus.COMPLETED);

        when(repository.findById(1L)).thenReturn(Optional.of(request));

        assertThrows(InvalidRequestStatusException.class, () -> service.cancelRequest(1L));
    }

    @Test
    void testUpdateCancelledRequestStatus_ThrowsException() {
        ServiceRequest request = new ServiceRequest();
        request.setId(1L);
        request.setStatus(RequestStatus.CANCELLED);

        when(repository.findById(1L)).thenReturn(Optional.of(request));

        assertThrows(InvalidRequestStatusException.class, () -> service.updateRequestStatus(1L, RequestStatus.ASSIGNED));
    }

    @Test
    void testConvertToResponse_PopulatesTechnicianInfo() {
        ServiceRequest request = new ServiceRequest(1L, 101L, 1L, "Pipe Repair", RequestStatus.ASSIGNED, "Hyderabad", LocalDateTime.now());

        AssignmentClient.AssignmentDto assignment = new AssignmentClient.AssignmentDto();
        assignment.setServiceRequestId(1L);
        assignment.setTechnicianId(202L);

        UserClient.UserDto user = new UserClient.UserDto();
        user.setUserId(202L);
        user.setName("John Doe");

        when(repository.findById(1L)).thenReturn(Optional.of(request));
        when(assignmentClient.getAllAssignments("ADMIN"))
            .thenReturn(ResponseEntity.ok(Collections.singletonList(assignment)));
        when(userClient.getUserProfile(202L, "ADMIN"))
            .thenReturn(ResponseEntity.ok(user));

        ServiceRequestDetailResponse response = service.getRequestById(1L);

        assertEquals(202L, response.getTechnicianId());
        assertEquals("John Doe", response.getTechnicianName());
    }
}