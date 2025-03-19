package jenius.tickitapi.performance;

import jenius.performanceservice.domain.PerformanceGenre;
import jenius.performanceservice.dto.request.PerformanceCreateRequestDto;
import jenius.performanceservice.dto.request.PerformanceScheduleDto;
import jenius.performanceservice.dto.request.PerformanceSearchRequestDto;
import jenius.performanceservice.dto.response.PerformanceCreateResponseDto;
import jenius.performanceservice.dto.response.PerformanceSearchResponseDto;
import jenius.performanceservice.repository.PerformanceRepository;
import jenius.performanceservice.service.PerformanceService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
class PerformanceControllerTest {

    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private PerformanceService performanceService;

    @Test
    void createPerformance() {
        // given
        PerformanceCreateRequestDto createRequestDto = PerformanceCreateRequestDto.builder()
                .title("테스트공연")
                .startDate(LocalDate.of(2025, 4, 2))
                .endDate(LocalDate.of(2025, 4, 3))
                .genre(PerformanceGenre.CONCERT)
                .location("AB홀")
                .schedules(List.of(
                        PerformanceScheduleDto.builder()
                                .performanceDate(LocalDate.of(2025, 4, 2))
                                .startTime(LocalTime.of(20, 00))
                                .availableSeats(100)
                                .build(),
                        PerformanceScheduleDto.builder()
                                .performanceDate(LocalDate.of(2025, 4, 3))
                                .startTime(LocalTime.of(17, 00))
                                .availableSeats(100)
                                .build()
                ))
                .build();

        // when
        PerformanceCreateResponseDto createResponseDto =
                performanceService.createPerformance(createRequestDto);

        // then
        Assertions.assertThat(createResponseDto.getPerformanceId()).isEqualTo(1L);
        Assertions.assertThat(createResponseDto.getTitle()).isEqualTo("테스트공연");
        Assertions.assertThat(createResponseDto.getPerformanceSchedule().get(0).getAvailableSeats())
                .isEqualTo(100);
    }

    @Test
    void findPerformance() {
        // given
        PerformanceCreateRequestDto createRequestDto_1 = PerformanceCreateRequestDto.builder()
                .title("A테스트공연")
                .startDate(LocalDate.of(2025, 4, 2))
                .endDate(LocalDate.of(2025, 4, 3))
                .genre(PerformanceGenre.CONCERT)
                .location("AB홀")
                .schedules(List.of(
                        PerformanceScheduleDto.builder()
                                .performanceDate(LocalDate.of(2025, 4, 2))
                                .startTime(LocalTime.of(20, 00))
                                .availableSeats(100)
                                .build(),
                        PerformanceScheduleDto.builder()
                                .performanceDate(LocalDate.of(2025, 4, 3))
                                .startTime(LocalTime.of(17, 00))
                                .availableSeats(100)
                                .build()
                ))
                .build();

        PerformanceCreateRequestDto createRequestDto_2 = PerformanceCreateRequestDto.builder()
                .title("B테스트공연")
                .startDate(LocalDate.of(2025, 5, 6))
                .endDate(LocalDate.of(2025, 5, 7))
                .genre(PerformanceGenre.CONCERT)
                .location("AB홀")
                .schedules(List.of(
                        PerformanceScheduleDto.builder()
                                .performanceDate(LocalDate.of(2025, 5, 6))
                                .startTime(LocalTime.of(20, 00))
                                .availableSeats(100)
                                .build(),
                        PerformanceScheduleDto.builder()
                                .performanceDate(LocalDate.of(2025, 5, 7))
                                .startTime(LocalTime.of(17, 00))
                                .availableSeats(100)
                                .build()
                ))
                .build();

        performanceService.createPerformance(createRequestDto_1);
        performanceService.createPerformance(createRequestDto_2);

        PerformanceSearchRequestDto searchRequestDto = PerformanceSearchRequestDto.builder()
                .keyword("테스트공연")
                .build();

        // when
        List<PerformanceSearchResponseDto> searchResponseDto =
                performanceService.searchPerformances(searchRequestDto);

        // then
        Assertions.assertThat(searchResponseDto.size()).isEqualTo(2);
        Assertions.assertThat(searchResponseDto.get(0).getTitle()).isEqualTo("A테스트공연");
        Assertions.assertThat(searchResponseDto.get(1).getTitle()).isEqualTo("B테스트공연");
    }
}