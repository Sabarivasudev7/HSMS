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
@Table(name = "technician_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianProfile {
    @Id
    private Long technicianId;

    private String skill;
    private Integer experience;
    private String phone;
    private Boolean availability;
    private Double rating;

    @OneToOne
    @MapsId
    private UserProfile userProfile;
}
