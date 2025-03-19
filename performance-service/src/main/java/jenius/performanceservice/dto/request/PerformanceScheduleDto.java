package jenius.performanceservice.dto.request;

import jenius.performanceservice.domain.PerformanceSchedule;
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
public class PerformanceScheduleDto {
    private LocalDate performanceDate;
    private LocalTime startTime;

    public static PerformanceScheduleDto fromEntity(PerformanceSchedule performanceSchedule) {
        return PerformanceScheduleDto.builder()
                .performanceDate(performanceSchedule.getPerformanceDate())
                .startTime(performanceSchedule.getStartTime())
                .build();
    }
}
