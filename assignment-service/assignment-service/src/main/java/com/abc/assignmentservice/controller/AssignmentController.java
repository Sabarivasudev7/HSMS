package com.abc.assignmentservice.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.abc.assignmentservice.dto.AssignmentDetailResponseDTO;
import com.abc.assignmentservice.dto.AssignmentRequestDTO;
import com.abc.assignmentservice.dto.AssignmentResponseDTO;
import com.abc.assignmentservice.enums.AssignmentStatus;
import com.abc.assignmentservice.enums.Roles;
import com.abc.assignmentservice.service.AssignmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private static final String ACCESS_DENIED = "Access Denied";

    private final AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<AssignmentResponseDTO> assignTechnician(@RequestHeader("X-Role") Roles role,
                                                                  @RequestHeader("X-User-Id") Long userId,
                                                                  @Valid @RequestBody AssignmentRequestDTO requestDTO) {
        requireRole(role, Roles.SERVICE_MANAGER);
        return ResponseEntity.status(HttpStatus.CREATED).body(assignmentService.assignTechnician(requestDTO, userId));
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<AssignmentDetailResponseDTO> getAssignmentById(@RequestHeader("X-Role") Roles role,
                                                                        @RequestHeader("X-User-Id") Long userId,
                                                                        @PathVariable Long assignmentId) {
        AssignmentDetailResponseDTO assignment = assignmentService.getAssignmentById(assignmentId);
        validateAssignmentViewer(role, userId, assignment.getTechnicianId());
        return ResponseEntity.ok(assignment);
    }

    @GetMapping
    public ResponseEntity<List<AssignmentDetailResponseDTO>> getAllAssignments(@RequestHeader("X-Role") Roles role) {
        requireAdminOrManager(role);
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<AssignmentResponseDTO>> getAssignmentsByTechnician(@RequestHeader("X-Role") Roles role,
                                                                                  @RequestHeader("X-User-Id") Long userId,
                                                                                  @PathVariable Long technicianId) {
        if (Roles.TECHNICIAN.equals(role) && !technicianId.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
        if (!Roles.TECHNICIAN.equals(role) && !Roles.ADMIN.equals(role) && !Roles.SERVICE_MANAGER.equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
        return ResponseEntity.ok(assignmentService.getAssignmentsByTechnician(technicianId));
    }

    @PatchMapping("/{assignmentId}/status")
    public ResponseEntity<AssignmentResponseDTO> updateStatus(@RequestHeader("X-Role") Roles role,
                                                              @PathVariable Long assignmentId,
                                                              @RequestParam AssignmentStatus status) {
        requireAdminOrManager(role);
        return ResponseEntity.ok(assignmentService.updateAssignmentStatus(assignmentId, status));
    }

    @PatchMapping("/{assignmentId}/reassign")
    public ResponseEntity<AssignmentResponseDTO> reassignTechnician(@RequestHeader("X-Role") Roles role,
                                                                    @RequestHeader("X-User-Id") Long managerId,
                                                                    @PathVariable Long assignmentId,
                                                                    @RequestParam Long technicianId) {
        requireRole(role, Roles.SERVICE_MANAGER);
        return ResponseEntity.ok(assignmentService.reassignTechnician(assignmentId, technicianId, managerId));
    }

    @PatchMapping("/{assignmentId}/accept")
    public ResponseEntity<AssignmentResponseDTO> acceptJob(@RequestHeader("X-Role") Roles role,
                                                           @RequestHeader("X-User-Id") Long userId,
                                                           @PathVariable Long assignmentId) {
        requireRole(role, Roles.TECHNICIAN);
        return ResponseEntity.ok(assignmentService.acceptJob(assignmentId, userId));
    }

    @PatchMapping("/{assignmentId}/reject")
    public ResponseEntity<AssignmentResponseDTO> rejectJob(@RequestHeader("X-Role") Roles role,
                                                           @RequestHeader("X-User-Id") Long userId,
                                                           @PathVariable Long assignmentId) {
        requireRole(role, Roles.TECHNICIAN);
        return ResponseEntity.ok(assignmentService.rejectJob(assignmentId, userId));
    }

    @GetMapping("/active-technicians/count")
    public ResponseEntity<Long> getActiveTechniciansCount(@RequestHeader("X-Role") Roles role,
                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        requireAdminOrManager(role);
        return ResponseEntity.ok(assignmentService.getActiveTechniciansCount(date));
    }

    private void validateAssignmentViewer(Roles role, Long userId, Long technicianId) {
        if (Roles.TECHNICIAN.equals(role) && technicianId.equals(userId)) {
            return;
        }
        if (Roles.ADMIN.equals(role) || Roles.SERVICE_MANAGER.equals(role)) {
            return;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
    }

    private void requireAdminOrManager(Roles role) {
        if (!Roles.ADMIN.equals(role) && !Roles.SERVICE_MANAGER.equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
    }

    private void requireRole(Roles actualRole, Roles expectedRole) {
        if (!expectedRole.equals(actualRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ACCESS_DENIED);
        }
    }
}
