package jenius.performanceservice.dto.response;

import jenius.performanceservice.dto.request.PerformanceScheduleDto;
import jenius.seatservice.dto.request.SeatSearchDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleWithSeatsDto {
    private PerformanceScheduleDto schedule;
    private List<SeatSearchDto> seats = new ArrayList<>();
}
