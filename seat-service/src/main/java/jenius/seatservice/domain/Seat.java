package jenius.seatservice.domain;

import jakarta.persistence.*;
import jenius.common.exception.CustomException;
import jenius.seatservice.exception.SeatErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long performanceScheduleId;

    private String seatNumber;

    @Enumerated(value = EnumType.STRING)
    private SeatType seatType;

    private Long price;

    private boolean isReserved;

    @Builder
    public Seat(Long performanceScheduleId, String seatNumber, SeatType seatType, Long price) {
        this.performanceScheduleId = performanceScheduleId;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.price = price;
        this.isReserved = false;
    }

    public void reserve() {
        if (isReserved) {
            throw new CustomException(SeatErrorCode.ALREADY_RESERVED_RESERVATION);
        }
        isReserved = true;
    }

    public void cancel() {
        if (!isReserved) {
            throw new CustomException(SeatErrorCode.ALREADY_CANCELED_RESERVATION);
        }
        isReserved = false;
    }

}
