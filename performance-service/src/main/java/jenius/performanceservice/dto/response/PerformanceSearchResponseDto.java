package jenius.performanceservice.dto.response;

import jenius.performanceservice.domain.Performance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceSearchResponseDto {

    private String keyword;
    private String title;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;

    public static List<PerformanceSearchResponseDto> fromEntity(String keyword, List<Performance> performances) {
        return performances.stream()
                .map(performance -> PerformanceSearchResponseDto.builder()
                        .keyword(keyword)
                        .title(performance.getTitle())
                        .location(performance.getLocation())
                        .startDate(performance.getStartDate())
                        .endDate(performance.getEndDate())
                        .build())
                .toList();
    }

}
