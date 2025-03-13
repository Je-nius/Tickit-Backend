package jenius.reservationservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long performanceId;

    private String reservationNumber;

    @Enumerated(value = EnumType.STRING)
    private ReservationStatus status;

    private LocalDateTime reservationDate;

    private LocalDateTime cancelDate;

    private int quantity;

    @Builder
    public Reservation(Long userId, Long performanceId, String reservationNumber, ReservationStatus status,
                       LocalDateTime reservationDate, int quantity) {
        this.userId = userId;
        this.performanceId = performanceId;
        this.reservationNumber = reservationNumber;
        this.status = status;
        this.reservationDate = reservationDate;
        this.quantity = quantity;
    }

    public void cancel() {
        // TODO: 취소 불가 기간 생기면 검증 로직 추가
        this.status = ReservationStatus.CANCELED;
        this.cancelDate = LocalDateTime.now();
    }

    public void reserve() {
        this.status = ReservationStatus.RESERVED;
        this.reservationDate = LocalDateTime.now();
    }

    public boolean isValidQuantity(int quantity, int maxQuantity) {
        return quantity > 0 && quantity <= maxQuantity;
    }

    public boolean isCanceled() {
        return this.status.equals(ReservationStatus.CANCELED);
    }

    public boolean isReserved() {
        return this.status.equals(ReservationStatus.RESERVED);
    }
}
