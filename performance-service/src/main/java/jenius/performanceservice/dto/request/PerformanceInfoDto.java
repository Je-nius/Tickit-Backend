package jenius.performanceservice.dto.request;

import jenius.performanceservice.domain.PerformanceInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceInfoDto {
    private LocalDate performanceDate;
    private LocalTime startTime;
    private Integer availableSeats;

    public static PerformanceInfoDto fromEntity(PerformanceInfo performanceInfo) {
        return PerformanceInfoDto.builder()
                .performanceDate(performanceInfo.getPerformanceDate())
                .startTime(performanceInfo.getStartTime())
                .availableSeats(performanceInfo.getAvailableSeats())
                .build();
    }
}
