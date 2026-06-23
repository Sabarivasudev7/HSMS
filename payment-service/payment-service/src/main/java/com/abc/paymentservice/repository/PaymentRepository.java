package com.abc.paymentservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abc.paymentservice.entity.Payment;
import com.abc.paymentservice.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByServiceRequestId(Long serviceRequestId);

    List<Payment> findByStatus(PaymentStatus status);

}