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
@Table(name = "customer_profiles")
public class CustomerProfile {
	@Id
    private Long customerId;

    private String name;

    private String phone;

    private String address;

    private String city;
    
    private String pincode;
}