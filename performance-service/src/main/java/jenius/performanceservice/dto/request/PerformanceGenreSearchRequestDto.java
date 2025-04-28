package jenius.performanceservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jenius.performanceservice.domain.PerformanceGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceGenreSearchRequestDto {
    @NotNull(message = "장르를 선택해주세요.")
    private PerformanceGenre genre;
}
