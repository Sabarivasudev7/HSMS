package com.hsms.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingNotificationDTO {

    private Long bookingId;

    private Long customerId;

    private String serviceName;

    private String bookingStatus;

}