package jenius.performanceservice.domain;

import jakarta.persistence.*;
import jenius.commonexception.CustomException;
import jenius.performanceservice.dto.PerformanceUpdateRequestDto;
import jenius.performanceservice.exception.PerformanceErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private int runningTime;

    @Enumerated(EnumType.STRING)
    private PerformanceGenre genre;

    private String location;

//    private String posterUrl;

    @Builder
    public Performance(String title, LocalDate startDate, LocalDate endDate, int runningTime,
                       PerformanceGenre genre, String location) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.runningTime = runningTime;
        this.genre = genre;
        this.location = location;

        validatePerformanceDate(startDate, endDate);
    }

    public void validatePerformanceDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new CustomException(PerformanceErrorCode.INVALID_PERFORMANCE_DATE);
        }
    }

    public void changePerformanceInfo(PerformanceUpdateRequestDto updateRequestDto) {
        if (updateRequestDto.getTitle() != null) this.title = updateRequestDto.getTitle();
        if (updateRequestDto.getRunningTime() >= 0) this.runningTime = updateRequestDto.getRunningTime();
        if (updateRequestDto.getGenre() != null) this.genre = updateRequestDto.getGenre();
        if (updateRequestDto.getLocation() != null) this.location = updateRequestDto.getLocation();

        this.startDate = (updateRequestDto.getStartDate() != null) ? updateRequestDto.getStartDate() : this.startDate;
        this.endDate = (updateRequestDto.getEndDate() != null) ? updateRequestDto.getEndDate() : this.endDate;

        validatePerformanceDate(this.startDate, this.endDate);
    }

}
