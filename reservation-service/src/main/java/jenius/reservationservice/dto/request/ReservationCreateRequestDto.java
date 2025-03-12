package jenius.reservationservice.dto.request;

import jakarta.validation.constraints.NotNull;
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
    private Long performanceId;
    @NotNull
    private int quantity;
    @NotNull
    private String payType;

}
