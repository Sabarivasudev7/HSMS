package com.abc.assignmentservice.service;

import java.time.LocalDate;
import java.util.List;

import com.abc.assignmentservice.dto.AssignmentDetailResponseDTO;
import com.abc.assignmentservice.dto.AssignmentRequestDTO;
import com.abc.assignmentservice.dto.AssignmentResponseDTO;
import com.abc.assignmentservice.enums.AssignmentStatus;

public interface AssignmentService {

	AssignmentResponseDTO assignTechnician(AssignmentRequestDTO requestDTO, Long managerId);

	AssignmentDetailResponseDTO getAssignmentById(Long assignmentId);

	List<AssignmentDetailResponseDTO> getAllAssignments();

	List<AssignmentResponseDTO> getAssignmentsByTechnician(Long technicianId);

	AssignmentResponseDTO updateAssignmentStatus(Long assignmentId, AssignmentStatus status);

	AssignmentResponseDTO reassignTechnician(Long assignmentId, Long technicianId, Long managerId);

	AssignmentResponseDTO acceptJob(Long assignmentId, Long userId);

	AssignmentResponseDTO rejectJob(Long assignmentId, Long userId);

	long getActiveTechniciansCount(LocalDate date);
}
