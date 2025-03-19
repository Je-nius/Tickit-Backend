package jenius.performanceservice.service;

import jenius.performanceservice.domain.PerformanceSchedule;
import jenius.performanceservice.domain.seat.Seat;
import jenius.performanceservice.domain.seat.SeatType;
import jenius.performanceservice.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    // 좌석 생성
    // SeatCreateDto 를 만들어도 됨.
    public void generateSeatsForPerformanceSchedule(PerformanceSchedule performanceSchedule) {
        List<Seat> seats = new ArrayList<>();

        char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        int seatsPerRow = 10; // 각 열당 좌석 개수

        for (char row : rows) {
            for (int num = 1; num <= seatsPerRow; num++) {
                SeatType seatType = SeatType.STANDARD;
                Double price = 90_000d;
                if (row == 'A' || row == 'B') {
                    seatType = SeatType.VIP;
                    price = 140_000d;
                }
                Seat seat = Seat.builder()
                        .performanceScheduleId(performanceSchedule.getId())
                        .seatNumber(row + String.valueOf(num))
                        .seatType(seatType)
                        .price(price)
                        .build();
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
    }

}
