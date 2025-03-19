package jenius.performanceservice.repository;

import jenius.performanceservice.domain.PerformanceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceScheduleRepository extends JpaRepository<PerformanceSchedule, Long> {
}
