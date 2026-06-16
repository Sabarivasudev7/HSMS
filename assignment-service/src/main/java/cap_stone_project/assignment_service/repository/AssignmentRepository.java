package cap_stone_project.assignment_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cap_stone_project.assignment_service.entity.Assignment;

@Repository
public interface AssignmentRepository
        extends JpaRepository<Assignment, Long>{

    Optional<Assignment> findByServiceRequestId(Long serviceRequestId);

    List<Assignment> findByTechnicianId(Long technicianId);
}