package jenius.reservationservice.dto.response;

import jenius.seatservice.domain.SeatType;

import java.time.LocalDate;

public class ReservationResponseDto {

    private String title;
    private LocalDate performanceDate;
    private String location;
    private SeatType seatType;
    private Integer quantity;

}
