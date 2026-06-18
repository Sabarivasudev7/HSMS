package cap_stone_project.booking_service.entity;

import java.time.LocalDateTime;

import cap_stone_project.booking_service.Enum.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "booking_tbl")
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long requestId;
	
	private String serviceType;
	
	@Enumerated(EnumType.STRING)
	private BookingStatus status;

	@Column(nullable = false)
	private String address;
	
	private LocalDateTime scheduleDateTime;
	
	private LocalDateTime createdAt;
	
	private Long categoryId;
	
	private Long customerId;
	
	private String assignmentStatus;
	

}
