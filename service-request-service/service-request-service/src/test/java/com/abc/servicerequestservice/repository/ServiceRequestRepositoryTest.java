package com.abc.servicerequestservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.abc.servicerequestservice.entity.ServiceRequest;
import com.abc.servicerequestservice.enums.RequestStatus;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ServiceRequestRepositoryTest {

    @Autowired
    private ServiceRequestRepository repository;

    @Test
    void testFindByCustomerId() {
        ServiceRequest request1 = new ServiceRequest(null, 101L, 1L, "Pipe Repair", RequestStatus.CREATED, "Hyderabad", LocalDateTime.now());
        ServiceRequest request2 = new ServiceRequest(null, 101L, 2L, "Wiring", RequestStatus.CREATED, "Hyderabad", LocalDateTime.now());
        ServiceRequest request3 = new ServiceRequest(null, 102L, 1L, "Pipe Repair", RequestStatus.CREATED, "Bangalore", LocalDateTime.now());

        repository.save(request1);
        repository.save(request2);
        repository.save(request3);

        Pageable pageable = PageRequest.of(0, 5);
        Page<ServiceRequest> page = repository.findByCustomerId(101L, pageable);

        assertNotNull(page);
        assertTrue(page.getTotalElements() >= 2);
        assertTrue(page.getContent().stream().allMatch(req -> req.getCustomerId().equals(101L)));
    }
}
