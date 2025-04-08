package jenius.reservationservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jenius.seatservice.domain.SeatType;

public class ReservationRequestDto {

    @NotNull
    private Long performanceScheduleId;
    @NotNull
    private SeatType seatType;
    @NotNull
    private Integer quantity;
//    @NotNull
//    private String payType;

}
