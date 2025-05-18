package jenius.performanceservice.dto.response;

import jenius.performanceservice.domain.Performance;
import jenius.performanceservice.domain.PerformanceGenre;
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
public class PerformanceGenreSearchResponseDto {

    private Long performanceId;
    private PerformanceGenre genre;
    private String title;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private String posterUrl;

    public static List<PerformanceGenreSearchResponseDto> fromEntity(PerformanceGenre genre,
                                                                     Map<Performance, String> performances) {

        return performances.entrySet().stream()
                .map(performance -> PerformanceGenreSearchResponseDto.builder()
                        .performanceId(performance.getKey().getId())
                        .genre(genre)
                        .title(performance.getKey().getTitle())
                        .location(performance.getKey().getLocation())
                        .startDate(performance.getKey().getStartDate())
                        .endDate(performance.getKey().getEndDate())
                        .posterUrl(performance.getValue())
                        .build())
                .toList();
    }

}
