package com.abc.servicerequestservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.abc.servicerequestservice.dto.ServiceRequestDetailResponse;
import com.abc.servicerequestservice.dto.ServiceRequestRequest;
import com.abc.servicerequestservice.dto.ServiceRequestUpdateDTO;
import com.abc.servicerequestservice.enums.RequestStatus;
import com.abc.servicerequestservice.service.ServiceRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ServiceRequestController.class)
class ServiceRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceRequestService service;

    @Test
    void testCreateRequest_Customer_Success() throws Exception {
        ServiceRequestRequest request = new ServiceRequestRequest(101L, 1L, "Pipe Repair", "Hyderabad", LocalDateTime.now());
        ServiceRequestDetailResponse response = new ServiceRequestDetailResponse(1L, 101L, 1L, "Pipe Repair", RequestStatus.CREATED, "Hyderabad", LocalDateTime.now(), null, null);

        when(service.createRequest(any(ServiceRequestRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/service-requests")
                .header("X-Role", "CUSTOMER")
                .header("X-User-Id", "101")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requestId").value(1L))
                .andExpect(jsonPath("$.status").value("CREATED"));

        verify(service, times(1)).createRequest(any(ServiceRequestRequest.class));
    }

    @Test
    void testCreateRequest_Technician_Forbidden() throws Exception {
        ServiceRequestRequest request = new ServiceRequestRequest(101L, 1L, "Pipe Repair", "Hyderabad", LocalDateTime.now());

        mockMvc.perform(post("/api/service-requests")
                .header("X-Role", "TECHNICIAN")
                .header("X-User-Id", "102")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        verify(service, never()).createRequest(any(ServiceRequestRequest.class));
    }

    @Test
    void testGetAllRequests_Admin_Success() throws Exception {
        ServiceRequestDetailResponse response = new ServiceRequestDetailResponse(1L, 101L, 1L, "Pipe Repair", RequestStatus.CREATED, "Hyderabad", LocalDateTime.now(), null, null);

        when(service.getAllRequests()).thenReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/service-requests")
                .header("X-Role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].requestId").value(1L));

        verify(service, times(1)).getAllRequests();
    }

    @Test
    void testGetAllRequests_Customer_Forbidden() throws Exception {
        mockMvc.perform(get("/api/service-requests")
                .header("X-Role", "CUSTOMER"))
                .andExpect(status().isForbidden());

        verify(service, never()).getAllRequests();
    }

    @Test
    void testGetRequestById_OwnerCustomer_Success() throws Exception {
        ServiceRequestDetailResponse response = new ServiceRequestDetailResponse(1L, 101L, 1L, "Pipe Repair", RequestStatus.CREATED, "Hyderabad", LocalDateTime.now(), null, null);

        when(service.getRequestById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/service-requests/{id}", 1L)
                .header("X-Role", "CUSTOMER")
                .header("X-User-Id", "101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestId").value(1L));

        verify(service, times(1)).getRequestById(1L);
    }

    @Test
    void testGetRequestById_NonOwnerCustomer_Forbidden() throws Exception {
        ServiceRequestDetailResponse response = new ServiceRequestDetailResponse(1L, 101L, 1L, "Pipe Repair", RequestStatus.CREATED, "Hyderabad", LocalDateTime.now(), null, null);

        when(service.getRequestById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/service-requests/{id}", 1L)
                .header("X-Role", "CUSTOMER")
                .header("X-User-Id", "102"))
                .andExpect(status().isForbidden());

        verify(service, times(1)).getRequestById(1L);
    }

    @Test
    void testGetRequestsByCustomer_Self_Success() throws Exception {
        ServiceRequestDetailResponse response = new ServiceRequestDetailResponse(1L, 101L, 1L, "Pipe Repair", RequestStatus.CREATED, "Hyderabad", LocalDateTime.now(), null, null);
        Page<ServiceRequestDetailResponse> page = new PageImpl<>(Collections.singletonList(response));

        when(service.getRequestsByCustomer(eq(101L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/service-requests/customer/{customerId}", 101L)
                .header("X-Role", "CUSTOMER")
                .header("X-User-Id", "101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].customerId").value(101L));

        verify(service, times(1)).getRequestsByCustomer(eq(101L), any(Pageable.class));
    }

    @Test
    void testGetRequestsByCustomer_OtherCustomer_Forbidden() throws Exception {
        mockMvc.perform(get("/api/service-requests/customer/{customerId}", 101L)
                .header("X-Role", "CUSTOMER")
                .header("X-User-Id", "102"))
                .andExpect(status().isForbidden());

        verify(service, never()).getRequestsByCustomer(anyLong(), any(Pageable.class));
    }

    @Test
    void testUpdateRequest_Owner_Success() throws Exception {
        ServiceRequestUpdateDTO updateDTO = new ServiceRequestUpdateDTO("Pipe Repair", "Hyderabad Updated", LocalDateTime.now());
        ServiceRequestDetailResponse response = new ServiceRequestDetailResponse(1L, 101L, 1L, "Pipe Repair", RequestStatus.CREATED, "Hyderabad Updated", LocalDateTime.now(), null, null);

        when(service.getRequestById(1L)).thenReturn(response);
        when(service.updateRequest(eq(1L), any(ServiceRequestUpdateDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/service-requests/{id}", 1L)
                .header("X-Role", "CUSTOMER")
                .header("X-User-Id", "101")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Hyderabad Updated"));

        verify(service, times(1)).updateRequest(eq(1L), any(ServiceRequestUpdateDTO.class));
    }

    @Test
    void testUpdateRequest_NonOwner_Forbidden() throws Exception {
        ServiceRequestUpdateDTO updateDTO = new ServiceRequestUpdateDTO("Pipe Repair", "Hyderabad Updated", LocalDateTime.now());
        ServiceRequestDetailResponse response = new ServiceRequestDetailResponse(1L, 101L, 1L, "Pipe Repair", RequestStatus.CREATED, "Hyderabad", LocalDateTime.now(), null, null);

        when(service.getRequestById(1L)).thenReturn(response);

        mockMvc.perform(put("/api/service-requests/{id}", 1L)
                .header("X-Role", "CUSTOMER")
                .header("X-User-Id", "102")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isForbidden());

        verify(service, never()).updateRequest(anyLong(), any(ServiceRequestUpdateDTO.class));
    }

    @Test
    void testUpdateStatus_Admin_Success() throws Exception {
        ServiceRequestDetailResponse response = new ServiceRequestDetailResponse(1L, 101L, 1L, "Pipe Repair", RequestStatus.ASSIGNED, "Hyderabad", LocalDateTime.now(), null, null);

        when(service.updateRequestStatus(1L, RequestStatus.ASSIGNED)).thenReturn(response);

        mockMvc.perform(patch("/api/service-requests/{id}/status", 1L)
                .header("X-Role", "ADMIN")
                .param("status", "ASSIGNED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ASSIGNED"));

        verify(service, times(1)).updateRequestStatus(1L, RequestStatus.ASSIGNED);
    }

    @Test
    void testCancelRequest_Owner_Success() throws Exception {
        ServiceRequestDetailResponse response = new ServiceRequestDetailResponse(1L, 101L, 1L, "Pipe Repair", RequestStatus.CREATED, "Hyderabad", LocalDateTime.now(), null, null);

        when(service.getRequestById(1L)).thenReturn(response);
        doNothing().when(service).cancelRequest(1L);

        mockMvc.perform(patch("/api/service-requests/{id}/cancel", 1L)
                .header("X-Role", "CUSTOMER")
                .header("X-User-Id", "101"))
                .andExpect(status().isOk())
                .andExpect(content().string("Request cancelled successfully"));

        verify(service, times(1)).cancelRequest(1L);
    }

    @Test
    void testCancelRequest_NonOwner_Forbidden() throws Exception {
        ServiceRequestDetailResponse response = new ServiceRequestDetailResponse(1L, 101L, 1L, "Pipe Repair", RequestStatus.CREATED, "Hyderabad", LocalDateTime.now(), null, null);

        when(service.getRequestById(1L)).thenReturn(response);

        mockMvc.perform(patch("/api/service-requests/{id}/cancel", 1L)
                .header("X-Role", "CUSTOMER")
                .header("X-User-Id", "102"))
                .andExpect(status().isForbidden());

        verify(service, never()).cancelRequest(anyLong());
    }
}
