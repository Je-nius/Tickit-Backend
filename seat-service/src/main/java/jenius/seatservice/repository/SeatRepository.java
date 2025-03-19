package jenius.seatservice.repository;

import jenius.seatservice.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    Long countByPerformanceScheduleId(Long performanceScheduleId);
}
