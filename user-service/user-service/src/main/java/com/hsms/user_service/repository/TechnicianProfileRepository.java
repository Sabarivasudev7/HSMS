package com.hsms.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hsms.user_service.entity.TechnicianProfile;

public interface TechnicianProfileRepository extends JpaRepository<TechnicianProfile, Long> {

}
