package jenius.performanceservice.dto.response;

import jenius.performanceservice.domain.Performance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    private String posterUrl;

    public static List<PerformanceSearchResponseDto> fromEntity(String keyword,
                                                                Map<Performance, String> performances) {

        return performances.entrySet().stream()
                .map(performance -> PerformanceSearchResponseDto.builder()
                        .keyword(keyword)
                        .title(performance.getKey().getTitle())
                        .location(performance.getKey().getLocation())
                        .startDate(performance.getKey().getStartDate())
                        .endDate(performance.getKey().getEndDate())
                        .posterUrl(performance.getValue())
                        .build())
                .toList();
    }

}
