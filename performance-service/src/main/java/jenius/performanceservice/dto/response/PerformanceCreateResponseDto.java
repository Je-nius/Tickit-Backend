package jenius.performanceservice.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jenius.performanceservice.domain.Performance;
import jenius.performanceservice.domain.PerformanceGenre;
import jenius.performanceservice.domain.PerformanceSchedule;
import jenius.performanceservice.dto.request.PerformanceScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PerformanceCreateResponseDto {

    private Long performanceId;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private int runningTime;

    @Enumerated(EnumType.STRING)
    private PerformanceGenre genre;

    private String location;

    private List<PerformanceScheduleDto> performanceSchedule;

    public static PerformanceCreateResponseDto fromEntity(Performance performance,
                                                          List<PerformanceSchedule> performanceInformation) {
        List<PerformanceScheduleDto> performanceScheduleDtoList = performanceInformation.stream()
                .map(PerformanceScheduleDto::fromEntity)
                .toList();

        return PerformanceCreateResponseDto.builder()
                .performanceId(performance.getId())
                .title(performance.getTitle())
                .startDate(performance.getStartDate())
                .endDate(performance.getEndDate())
                .runningTime(performance.getRunningTime())
                .genre(performance.getGenre())
                .location(performance.getLocation())
                .performanceSchedule(performanceScheduleDtoList)
                .build();
    }

}
