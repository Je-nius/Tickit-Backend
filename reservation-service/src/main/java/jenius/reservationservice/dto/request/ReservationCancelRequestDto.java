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
public class ReservationCancelRequestDto {

    // TODO: 결제 서비스가 추가 시, 결제 데이터도 필요

    @NotNull
    private Long reservationId;

}
