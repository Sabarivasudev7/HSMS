package com.hsms.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hsms.user_service.entity.CustomerProfile;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Long>{

}