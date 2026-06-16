package cap_stone_project.booking_service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cap_stone_project.booking_service.Enum.BookingStatus;
import cap_stone_project.booking_service.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{

//	List<Booking> findByCustomerId(Long customerId);
	Page<Booking> findByCustomerId(Long customerId, Pageable pageable);

	List<Booking> findByStatus(BookingStatus status);
	
	List<Booking> findAllByOrderByRequestIdDesc();
	
	

}
