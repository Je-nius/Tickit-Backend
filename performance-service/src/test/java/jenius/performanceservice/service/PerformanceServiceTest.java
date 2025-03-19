package jenius.performanceservice.service;

import jenius.performanceservice.domain.Performance;
import jenius.performanceservice.domain.PerformanceGenre;
import jenius.performanceservice.domain.PerformanceSchedule;
import jenius.performanceservice.dto.request.PerformanceCreateRequestDto;
import jenius.performanceservice.dto.request.PerformanceScheduleDto;
import jenius.performanceservice.dto.request.PerformanceSearchRequestDto;
import jenius.performanceservice.dto.response.PerformanceCreateResponseDto;
import jenius.performanceservice.dto.response.PerformanceSearchResponseDto;
import jenius.performanceservice.repository.PerformanceScheduleRepository;
import jenius.performanceservice.repository.PerformanceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerformanceServiceTest {

    @Mock
    PerformanceRepository performanceRepository;
    @Mock
    PerformanceScheduleRepository performanceScheduleRepository;

    @InjectMocks
    PerformanceService performanceService;

    // 기본 공연 CRUD Test 는 제외

    @Test
    @DisplayName("관리자는 공연을 생성할 수 있다.")
    public void createPerformance() {

        // given
        PerformanceScheduleDto infoDto1 = PerformanceScheduleDto.builder()
                .performanceDate(LocalDate.of(2025, 4, 15))
                .startTime(LocalTime.of(20, 0))
                .availableSeats(1000)
                .build();

        PerformanceScheduleDto infoDto2 = PerformanceScheduleDto.builder()
                .performanceDate(LocalDate.of(2025, 4, 16))
                .startTime(LocalTime.of(19, 0))
                .availableSeats(1000)
                .build();

        PerformanceCreateRequestDto createRequestDto = PerformanceCreateRequestDto.builder()
                .title("테스트 공연")
                .startDate(LocalDate.of(2025, 4, 15))
                .endDate(LocalDate.of(2025, 4, 16))
                .genre(PerformanceGenre.FESTIVAL)
                .location("어쩌구홀")
                .schedules(List.of(infoDto1, infoDto2))
                .build();

        PerformanceSchedule performanceSchedule1 = PerformanceSchedule.builder()
                .performanceId(1L)
                .performanceDate(LocalDate.of(2025, 4, 15))
                .startTime(LocalTime.of(20, 0))
                .availableSeats(1000)
                .build();

        PerformanceSchedule performanceSchedule2 = PerformanceSchedule.builder()
                .performanceId(1L)
                .performanceDate(LocalDate.of(2025, 4, 16))
                .startTime(LocalTime.of(19, 0))
                .availableSeats(1000)
                .build();

        Performance performance = Performance.builder()
                .title("테스트 공연")
                .startDate(LocalDate.of(2025, 4, 15))
                .endDate(LocalDate.of(2025, 4, 16))
                .genre(PerformanceGenre.FESTIVAL)
                .location("어쩌구홀")
                .build();

        when(performanceRepository.save(any())).thenReturn(performance);
        when(performanceScheduleRepository.saveAll(any())).thenReturn(List.of(performanceSchedule1, performanceSchedule2));

        // when
        PerformanceCreateResponseDto createResponseDto =
                performanceService.createPerformance(createRequestDto);

        // then
        Assertions.assertThat(createResponseDto).isNotNull();
        Assertions.assertThat(createResponseDto.getPerformanceSchedule().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("검색어로 공연을 조회할 수 있다.")
    public void searchPerformancesByTitle() {

        // given
        String keyword = "테스트공연";

        PerformanceSearchRequestDto performanceSearchRequestDto
                = PerformanceSearchRequestDto.builder()
                .keyword(keyword)
                .build();

        Performance performance_1 = Performance.builder()
                .title("테스트공연")
                .startDate(LocalDate.of(2025, 4, 15))
                .endDate(LocalDate.of(2025, 4, 16))
                .location("A홀")
                .build();

        Performance performance_2 = Performance.builder()
                .title("테스트공연-강원")
                .startDate(LocalDate.of(2025, 5, 9))
                .endDate(LocalDate.of(2025, 7, 21))
                .location("B홀")
                .build();


        when(performanceRepository.findByTitleOrLocation(keyword)).thenReturn(List.of(performance_1, performance_2));

        // when
        List<PerformanceSearchResponseDto> searchResponseDto =
                performanceService.searchPerformances(performanceSearchRequestDto);

        // then
        Assertions.assertThat(searchResponseDto).isNotNull();
        Assertions.assertThat(searchResponseDto.size()).isEqualTo(2);
        Assertions.assertThat(searchResponseDto.get(0).getTitle()).isEqualTo("테스트공연");
        Assertions.assertThat(searchResponseDto.get(1).getTitle()).isEqualTo("테스트공연-강원");
    }

}
