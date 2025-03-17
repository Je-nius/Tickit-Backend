package jenius.performanceservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceSearchRequestDto {
    @NotNull(message = "검색어를 입력해주세요.")
    private String keyword;
}
