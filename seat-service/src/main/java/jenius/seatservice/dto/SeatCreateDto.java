package jenius.seatservice.dto;

import jenius.seatservice.domain.SeatType;
import lombok.*;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SeatCreateDto {
    private Map<String, Integer> zoneSeatNumber;
    private Map<String, SeatType> zoneType;
    private Map<SeatType, Long> typePrice;
}
