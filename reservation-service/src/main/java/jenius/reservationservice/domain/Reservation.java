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

    private Long performanceScheduleId;

    private String reservationNumber;

    @Enumerated(value = EnumType.STRING)
    private ReservationStatus status;

    private LocalDateTime reservationDate;

    private LocalDateTime cancelDate;

    private Long totalAmount;

    @Builder
    public Reservation(Long userId, Long performanceScheduleId, String reservationNumber, ReservationStatus status,
                       LocalDateTime reservationDate, Long totalAmount) {
        this.userId = userId;
        this.performanceScheduleId = performanceScheduleId;
        this.reservationNumber = reservationNumber;
        this.status = status;
        this.reservationDate = reservationDate;
        this.totalAmount = totalAmount;
    }

    public void assignTotalAmount(Long amount) {
        this.totalAmount = amount;
    }

    public void pending() {
        this.status = ReservationStatus.PENDING;
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
