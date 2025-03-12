package jenius.reservationservice.dto.response;

import jenius.reservationservice.domain.Reservation;
import jenius.reservationservice.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCancelResponseDto {

    private String reservationNumber;
    private LocalDateTime cancelDate;
    private ReservationStatus reservationStatus;

    public static ReservationCancelResponseDto fromEntity(Reservation reservation) {
        return ReservationCancelResponseDto.builder()
                .reservationNumber(reservation.getReservationNumber())
                .cancelDate(reservation.getCancelDate())
                .reservationStatus(reservation.getStatus())
                .build();
    }

}
