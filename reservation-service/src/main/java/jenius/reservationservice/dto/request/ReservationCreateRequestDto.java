package jenius.reservationservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jenius.seatservice.domain.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCreateRequestDto {
    @NotNull
    private Long performanceScheduleId;
    @NotNull
    private SeatType seatType;
    @NotNull
    private int quantity;
//    @NotNull
//    private String payType;
}
