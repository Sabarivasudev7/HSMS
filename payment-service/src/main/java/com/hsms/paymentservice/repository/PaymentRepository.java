package com.hsms.paymentservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hsms.paymentservice.entity.Payment;
import com.hsms.paymentservice.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByServiceRequestId(Long serviceRequestId);

    List<Payment> findByCustomerId(Long customerId);

    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);

    Page<Payment> findByCustomerId(
            Long customerId,
            Pageable pageable);

}