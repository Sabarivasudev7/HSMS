package com.hsms.user_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfile {
    @Id
    private Long customerId;

    private String phone;
    private String address;
    private String city;
    private String pincode;

    @OneToOne
    @MapsId
    private UserProfile userProfile;
}
