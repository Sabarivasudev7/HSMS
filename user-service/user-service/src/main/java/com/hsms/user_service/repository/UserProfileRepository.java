package com.hsms.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hsms.user_service.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{

}
