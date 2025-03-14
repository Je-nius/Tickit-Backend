package jenius.performanceservice.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jenius.performanceservice.domain.Performance;
import jenius.performanceservice.domain.PerformanceGenre;
import jenius.performanceservice.domain.PerformanceInfo;
import jenius.performanceservice.dto.request.PerformanceInfoDto;
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

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private int runningTime;

    @Enumerated(EnumType.STRING)
    private PerformanceGenre genre;

    private String location;

    private List<PerformanceInfoDto> performanceInformation;

    public static PerformanceCreateResponseDto fromEntity(Performance performance,
                                                          List<PerformanceInfo> performanceInformation) {
        List<PerformanceInfoDto> performanceInfoDtoList = performanceInformation.stream()
                .map(PerformanceInfoDto::fromEntity)
                .toList();

        return PerformanceCreateResponseDto.builder()
                .title(performance.getTitle())
                .startDate(performance.getStartDate())
                .endDate(performance.getEndDate())
                .runningTime(performance.getRunningTime())
                .genre(performance.getGenre())
                .location(performance.getLocation())
                .performanceInformation(performanceInfoDtoList)
                .build();
    }

}
