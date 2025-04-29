package jenius.performanceservice.repository;

import jenius.performanceservice.domain.PerformanceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceScheduleRepository extends JpaRepository<PerformanceSchedule, Long> {
    List<PerformanceSchedule> findAllByPerformanceId(Long performanceId);
}
