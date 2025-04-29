package jenius.seatservice.service;

import jenius.common.exception.CustomException;
import jenius.seatservice.domain.Seat;
import jenius.seatservice.domain.SeatType;
import jenius.seatservice.dto.request.SeatCreateDto;
import jenius.seatservice.dto.request.SeatSearchDto;
import jenius.seatservice.exception.SeatErrorCode;
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

    /**
     * 좌석 생성 (공연 생성 시 사용)
     * @param performanceScheduleId
     * @param seatCreateDto
     */
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
                        .zone(zone)
                        .seatNumber(zone + i)
                        .seatType(zoneType)
                        .remainingSeats(zoneSeatCnt)
                        .price(zonePrice)
                        .build();

                seats.add(seat);
            }
        }

        seatRepository.saveAll(seats);
    }

    /**
     * 공연 이용 가능 좌석 수 세기 (일정 별)
     * @param performanceScheduleId
     * @return
     */
    public Long countAvailableSeat(Long performanceScheduleId) {
        return seatRepository.countByPerformanceScheduleId(performanceScheduleId);
    }

    /**
     * 이용 가능 좌석 중 가장 앞 좌석 반환
     */
    public Seat findFirstAvailableSeat(Long performanceScheduleId, SeatType seatType) {
        return seatRepository.findFirstByPerformanceScheduleIdAndSeatType(performanceScheduleId, seatType)
                .orElseThrow(() -> checkSeatExistence(performanceScheduleId));
    }

    public List<SeatSearchDto> findSeatByPerformanceScheduleId(Long performanceScheduleId) {
        return seatRepository.findByPerformanceScheduleId(performanceScheduleId);
    }

    /**
     * 좌석 가격 반환하기
     * @param seatId
     * @return
     */
    public Long getSeatPrice(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new CustomException(SeatErrorCode.NOT_FOUND_SEAT));
        return seat.getPrice();
    }

    private CustomException checkSeatExistence(Long performanceScheduleId) {
        if (!seatRepository.existsByPerformanceScheduleId(performanceScheduleId)) {
            return new CustomException(SeatErrorCode.NOT_FOUND_PERFORMANCE_SEAT);
        }
        return new CustomException(SeatErrorCode.NOT_FOUND_SEAT_TYPE);
    }

}
