package jenius.seatservice.service;

import jenius.seatservice.domain.Seat;
import jenius.seatservice.domain.SeatType;
import jenius.seatservice.dto.SeatCreateDto;
import jenius.seatservice.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    // 좌석 생성
    public void generateSeatsForPerformanceSchedule(Long performanceScheduleId,
                                                    SeatCreateDto seatCreateDto) {
        List<Seat> seats = new ArrayList<>();

        for (Map.Entry<String, Integer> zoneSeatNumber : seatCreateDto.getZoneSeatNumber().entrySet()) {
            String zone = zoneSeatNumber.getKey();
            int zoneSeatCnt = zoneSeatNumber.getValue();

            SeatType zoneType = seatCreateDto.getZoneType().get(zone);
            Long zonePrice = seatCreateDto.getTypePrice().get(zoneType);

            for (int i = 1; i <= zoneSeatCnt; i++) {
                Seat seat = Seat.builder()
                        .performanceScheduleId(performanceScheduleId)
                        .seatNumber(zoneType.toString() + i)
                        .seatType(zoneType)
                        .price(zonePrice)
                        .build();

                seats.add(seat);
            }
        }

        seatRepository.saveAll(seats);
    }

    // 좌석 수 count
    public Long countAvailableSeat(Long performanceScheduleId) {
        return seatRepository.countByPerformanceScheduleId(performanceScheduleId);
    }

}
