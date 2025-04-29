package jenius.performanceservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jenius.performanceservice.domain.PerformanceGenre;
import jenius.performanceservice.domain.PerformanceSchedule;
import jenius.seatservice.dto.request.SeatCreateDto;
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
public class PerformanceCreateRequestDto {

    @NotBlank
    private String title;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Positive
    private int runningTime; // 분 단위

    @NotNull
    private PerformanceGenre genre;

    @NotBlank
    private String location;

    private List<PerformanceScheduleDto> schedules;

    private SeatCreateDto seatConfig;

    @NotBlank
    private String artists;

    public List<PerformanceSchedule> toEntity(Long performanceId) {

        return schedules.stream()
                .map(schedule ->
                        PerformanceSchedule.builder()
                                .performanceId(performanceId)
                                .performanceDate(schedule.getPerformanceDate())
                                .startTime(schedule.getStartTime())
                                .build())
                .toList();
    }
}
