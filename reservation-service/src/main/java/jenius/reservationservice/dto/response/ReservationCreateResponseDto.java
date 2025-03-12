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
public class ReservationCreateResponseDto {

    private String performanceTitle;
    private String reservationNumber;
    private ReservationStatus reservationStatus;
    private LocalDateTime reservationDate;

    public static ReservationCreateResponseDto fromEntity(String performanceTitle, Reservation reservation) {
        return ReservationCreateResponseDto.builder()
                .performanceTitle(performanceTitle)
                .reservationNumber(reservation.getReservationNumber())
                .reservationStatus(reservation.getStatus())
                .reservationDate(reservation.getReservationDate())
                .build();
    }

}
