package jenius.seatservice.repository;

import jenius.seatservice.domain.Seat;
import jenius.seatservice.domain.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    boolean existsByPerformanceScheduleId(Long performanceScheduleId);
    Optional<Seat> findByPerformanceScheduleIdAndSeatType(Long performanceScheduleId, SeatType seatType);
    Long countByPerformanceScheduleId(Long performanceScheduleId);
}
