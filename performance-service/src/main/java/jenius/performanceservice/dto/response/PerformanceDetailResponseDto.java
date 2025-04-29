package jenius.performanceservice.dto.response;

import jenius.performanceservice.domain.PerformanceGenre;
import jenius.performanceservice.dto.request.PerformanceScheduleDto;
import jenius.seatservice.dto.request.SeatSearchDto;
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
public class PerformanceDetailResponseDto {

    private String posterUrl;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private PerformanceGenre genre;
    private String artists;
    private int runningTime;
    private List<ScheduleWithSeatsDto> scheduleWithSeatsDtos;

}
