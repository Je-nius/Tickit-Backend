package jenius.performanceservice.domain;

import jenius.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PerformanceTest {

    @Test
    @DisplayName("공연을 생성할 수 있다.")
    public void createPerformance() {

        LocalDate startDate = LocalDate.of(2025, 3, 10);
        LocalDate endDate = LocalDate.of(2025, 3, 13);

        Performance performance = Performance.builder()
                .title("테스트 공연")
                .startDate(startDate)
                .endDate(endDate)
                .genre(PerformanceGenre.CONCERT)
                .location("올림픽공원")
                .build();

        assertThat(performance).isNotNull();
    }

    @Test
    @DisplayName("공연 생성 시 시작일이 종료일 보다 늦으면 예외 발생")
    public void createPerformanceWithInvalidDate_Exception() {

        LocalDate startDate = LocalDate.of(2025, 3, 13);
        LocalDate endDate = LocalDate.of(2025, 3, 10);

        assertThrows(CustomException.class, () -> Performance.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .build(),
                "공연 시작일은 종료일보다 앞서야 힙니다.");
    }
    
    @Test
    @DisplayName("공연을 예매하면 이용 가능 좌석 수가 줄어든다")
    public void diffAvailableSeats() {

        int availableSeats = 100;
        int quantity = 2;

        PerformanceSchedule performanceSchedule = PerformanceSchedule.builder()
                .availableSeats(availableSeats)
                .build();

        performanceSchedule.reserve(quantity);

        assertEquals(availableSeats - quantity, performanceSchedule.getAvailableSeats());
    }

    @Test
    @DisplayName("공연을 취소하면 이용 가능 좌석 수가 늘어난다")
    public void addAvailableSeats() {

        int availableSeats = 98;
        int quantity = 2;

        PerformanceSchedule performanceSchedule = PerformanceSchedule.builder()
                .availableSeats(availableSeats)
                .build();

        performanceSchedule.cancel(quantity);

        assertEquals(availableSeats + quantity, performanceSchedule.getAvailableSeats());
    }

}