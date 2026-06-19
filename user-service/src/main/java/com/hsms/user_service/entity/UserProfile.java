package com.hsms.user_service.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @Id
    private Long userId;

    private String name;
    private String email;
    private String role;

    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private CustomerProfile customerProfile;

    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private TechnicianProfile technicianProfile;
}
