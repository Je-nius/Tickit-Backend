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

    private String zone; // 구역

    private String seatNumber; // 좌석 번호

    @Enumerated(value = EnumType.STRING)
    private SeatType seatType; // 좌석 유형

    private Long price; // 가격

    private int remainingSeats; // 잔여 좌석 수

    private boolean isReserved;

    @Builder
    public Seat(Long performanceScheduleId, String zone, String seatNumber, SeatType seatType,
                int remainingSeats, Long price) {
        this.performanceScheduleId = performanceScheduleId;
        this.zone = zone;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.remainingSeats = remainingSeats;
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
