package jenius.performanceservice.domain.seat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long performanceScheduleId;

    private String seatNumber;

    @Enumerated(value = EnumType.STRING)
    private SeatType seatType;

    private Double price;

    @Builder
    public Seat(Long performanceScheduleId, String seatNumber, SeatType seatType, Double price) {
        this.performanceScheduleId = performanceScheduleId;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.price = price;
    }

}
