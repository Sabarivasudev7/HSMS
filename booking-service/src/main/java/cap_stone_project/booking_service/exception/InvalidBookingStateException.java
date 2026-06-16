package cap_stone_project.booking_service.exception;

public class InvalidBookingStateException extends RuntimeException{
	public InvalidBookingStateException(String msg) {
		super(msg);
	}

}
