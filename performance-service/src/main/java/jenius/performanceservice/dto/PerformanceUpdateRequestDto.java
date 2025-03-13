package jenius.performanceservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jenius.performanceservice.domain.PerformanceGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceUpdateRequestDto {

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private int runningTime;

    private PerformanceGenre genre;

    private String location;

}
