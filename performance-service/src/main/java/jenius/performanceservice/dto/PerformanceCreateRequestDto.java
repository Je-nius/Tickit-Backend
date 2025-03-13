package jenius.performanceservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
public class PerformanceCreateRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Positive
    private int runningTime; // 분 단위

    @NotBlank
    private PerformanceGenre genre;

    @NotBlank
    private String location;

    @Positive
    private int availableSeats;

}
