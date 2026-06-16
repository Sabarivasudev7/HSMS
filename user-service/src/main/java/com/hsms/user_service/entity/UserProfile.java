package com.hsms.user_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "user_profiles")
public class UserProfile {
	
    @Id
    private Long userId;
    private String email;
    private String role;
    
    private Long customerId;

    private Long technicianId;
}