package cap_stone_project.assignment_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor


@Entity
@Table(name = "assignment_tbl")
public class Assignment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long assignmentId;
	
	private LocalDateTime assignedDate;
	
	private Long technicianId;
	
//	private Long bookingId;
	
//	private Long categoryId;

}
