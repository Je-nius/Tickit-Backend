package jenius.tickitapi.performance;

import jenius.performanceservice.domain.PerformanceGenre;
import jenius.performanceservice.dto.request.PerformanceCreateRequestDto;
import jenius.performanceservice.dto.request.PerformanceScheduleDto;
import jenius.performanceservice.dto.request.PerformanceSearchRequestDto;
import jenius.performanceservice.dto.response.PerformanceCreateResponseDto;
import jenius.performanceservice.dto.response.PerformanceSearchResponseDto;
import jenius.performanceservice.service.PerformanceService;
import jenius.seatservice.domain.SeatType;
import jenius.seatservice.dto.SeatCreateDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Transactional
class PerformanceIntegrationTest {

    @Autowired
    private PerformanceService performanceService;
    private MockMultipartFile file = new MockMultipartFile(
            "공연 포스터 이미지",
            "poster.png",
            MediaType.IMAGE_PNG_VALUE,
            "poster".getBytes()
    );

    @Test
    @DisplayName("공연을 생성할 수 있다.")
    void createPerformance() throws IOException {

        // given
        LocalDate startDate = LocalDate.of(2025, 4, 2);
        LocalDate endDate = LocalDate.of(2025, 4, 3);

        PerformanceCreateRequestDto createRequestDto = PerformanceCreateRequestDto.builder()
                .title("테스트공연")
                .startDate(startDate)
                .endDate(endDate)
                .genre(PerformanceGenre.CONCERT)
                .location("AB홀")
                .schedules(List.of(
                        PerformanceScheduleDto.builder()
                                .performanceDate(startDate)
                                .startTime(LocalTime.of(20, 00))
                                .build(),
                        PerformanceScheduleDto.builder()
                                .performanceDate(endDate)
                                .startTime(LocalTime.of(17, 00))
                                .build()
                ))
                .seatConfig(SeatCreateDto.builder()
                        .zoneSeatNumber(Map.of("A", 10, "B", 20)) // 총 30 좌석
                        .zoneType(Map.of("A", SeatType.VIP, "B", SeatType.STANDARD))
                        .typePrice(Map.of(SeatType.VIP, 140_000L, SeatType.STANDARD, 100_000L))
                        .build())
                .build();

        // when
        PerformanceCreateResponseDto createResponseDto =
                performanceService.createPerformance(file, createRequestDto);

        // then
        Assertions.assertThat(createResponseDto.getPerformanceId()).isEqualTo(1L);
        Assertions.assertThat(createResponseDto.getTitle()).isEqualTo("테스트공연");
        Assertions.assertThat(createResponseDto.getTotalSeatNumber().get(startDate))
                .isEqualTo(30);
    }

    @Test
    @DisplayName("공연을 조회할 수 있다.")
    void findPerformance() throws IOException {
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
                                .build(),
                        PerformanceScheduleDto.builder()
                                .performanceDate(LocalDate.of(2025, 4, 3))
                                .startTime(LocalTime.of(17, 00))
                                .build()
                ))
                .seatConfig(SeatCreateDto.builder()
                        .zoneSeatNumber(Map.of("A", 10, "B", 20))
                        .zoneType(Map.of("A", SeatType.VIP, "B", SeatType.STANDARD))
                        .typePrice(Map.of(SeatType.VIP, 140_000L, SeatType.STANDARD, 100_000L))
                        .build())
                .artists("아티스트1,아티스트2")
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
                                .build(),
                        PerformanceScheduleDto.builder()
                                .performanceDate(LocalDate.of(2025, 5, 7))
                                .startTime(LocalTime.of(17, 00))
                                .build()
                ))
                .seatConfig(SeatCreateDto.builder()
                        .zoneSeatNumber(Map.of("A", 10, "B", 20))
                        .zoneType(Map.of("A", SeatType.VIP, "B", SeatType.STANDARD))
                        .typePrice(Map.of(SeatType.VIP, 140_000L, SeatType.STANDARD, 100_000L))
                        .build())
                .artists("아티스트1,아티스트2")
                .build();

        performanceService.createPerformance(file, createRequestDto_1);
        performanceService.createPerformance(file, createRequestDto_2);

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
        Assertions.assertThat(searchResponseDto.get(0).getPosterUrl()).isNotNull();
        Assertions.assertThat(searchResponseDto.get(1).getPosterUrl()).isNotNull();
    }
}