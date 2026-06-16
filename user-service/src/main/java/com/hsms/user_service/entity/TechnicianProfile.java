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
@Table(name = "technician_profiles")
public class TechnicianProfile {
    @Id
    private Long  technicianId;
    private String skill;
    private Integer experience;
    private String phone;
    private Boolean availability;
    private Double rating;
}