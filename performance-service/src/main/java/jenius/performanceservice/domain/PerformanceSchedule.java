package jenius.performanceservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jenius.common.exception.CustomException;
import jenius.performanceservice.exception.PerformanceErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@Getter
public class PerformanceSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long performanceId;

    private LocalDate performanceDate;

    private LocalTime startTime;
    
    @Builder
    public PerformanceSchedule(Long performanceId, LocalDate performanceDate, LocalTime startTime, int availableSeats) {
        this.performanceId = performanceId;
        this.performanceDate = performanceDate;
        this.startTime = startTime;
    }

    public void validatePerformanceDate(LocalDate startDate, LocalDate endDate) {
        if (!(performanceDate.isEqual(startDate) ||
                (performanceDate.isAfter(startDate) && performanceDate.isBefore(endDate)) ||
                performanceDate.isEqual(endDate))) {
            throw new CustomException(PerformanceErrorCode.INVALID_PERFORMANCE_DATE);
        }
    }

    public void updateAvailableSeats(int availableSeats) {
        if (availableSeats < 0) {
            throw new CustomException(PerformanceErrorCode.INVALID_PERFORMANCE_SEAT);
        }
        this.availableSeats = availableSeats;
    }

    public boolean isReservable(int quantity) {
        return availableSeats >= quantity;
    }

    public void reserve(int quantity) {
        if (!isReservable(quantity)) {
            throw new CustomException(PerformanceErrorCode.FULL_SEAT_EXCEPTION);
        }
        availableSeats -= quantity;
    }

    public void cancel(int quantity) {
        availableSeats += quantity;
    }

}
