package jenius.performanceservice.repository;

import jenius.performanceservice.domain.seat.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    Long countByPerformanceScheduleId(Long performanceScheduleId);

}
