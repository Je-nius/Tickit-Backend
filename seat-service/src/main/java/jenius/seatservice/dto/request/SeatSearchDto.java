package jenius.seatservice.dto.request;

import jenius.seatservice.domain.Seat;
import jenius.seatservice.domain.SeatType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SeatSearchDto {
    private String zone; // 구역
    private SeatType seatType; // 좌석 타입
    private Long price; // 가격
    private long remainingSeats; // 좌석 수

    public static SeatSearchDto fromEntity(Seat seat) {
        return SeatSearchDto.builder()
                .zone(seat.getZone())
                .seatType(seat.getSeatType())
                .price(seat.getPrice())
                .remainingSeats(seat.getRemainingSeats())
                .build();
    }
}
