package jenius.performanceservice.service;

import jenius.performanceservice.domain.Performance;
import jenius.performanceservice.domain.PerformanceGenre;
import jenius.performanceservice.domain.PerformanceInfo;
import jenius.performanceservice.dto.request.PerformanceCreateRequestDto;
import jenius.performanceservice.dto.request.PerformanceInfoDto;
import jenius.performanceservice.dto.response.PerformanceCreateResponseDto;
import jenius.performanceservice.repository.PerformanceInfoRepository;
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
    PerformanceInfoRepository performanceInfoRepository;

    @InjectMocks
    PerformanceService performanceService;

    // 기본 공연 CRUD Test 는 제외

    @Test
    @DisplayName("관리자는 공연을 생성할 수 있다.")
    public void createPerformance() {

        // given
        PerformanceInfoDto infoDto1 = PerformanceInfoDto.builder()
                .performanceDate(LocalDate.of(2025, 4, 15))
                .startTime(LocalTime.of(20, 0))
                .availableSeats(1000)
                .build();

        PerformanceInfoDto infoDto2 = PerformanceInfoDto.builder()
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
                .information(List.of(infoDto1, infoDto2))
                .build();

        PerformanceInfo performanceInfo1 = PerformanceInfo.builder()
                .performanceId(1L)
                .performanceDate(LocalDate.of(2025, 4, 15))
                .startTime(LocalTime.of(20, 0))
                .availableSeats(1000)
                .build();

        PerformanceInfo performanceInfo2 = PerformanceInfo.builder()
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
        when(performanceInfoRepository.saveAll(any())).thenReturn(List.of(performanceInfo1, performanceInfo2));

        // when
        PerformanceCreateResponseDto createResponseDto =
                performanceService.createPerformance(createRequestDto);

        // then
        Assertions.assertThat(createResponseDto).isNotNull();
        Assertions.assertThat(createResponseDto.getPerformanceInformation().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("공연 타이틀로 공연을 조회할 수 있다.")
    public void findPerformanceByTitle() {


    }


}
