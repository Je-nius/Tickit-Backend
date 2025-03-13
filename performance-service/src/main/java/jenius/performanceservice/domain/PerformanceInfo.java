package jenius.performanceservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jenius.commonexception.CustomException;
import jenius.performanceservice.exception.PerformanceErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class PerformanceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long performanceId;

    private LocalDateTime performanceDate;

    private int availableSeats;

    @Builder
    public PerformanceInfo(LocalDateTime performanceDate, int availableSeats) {
        this.performanceDate = performanceDate;
        this.availableSeats = availableSeats;
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
