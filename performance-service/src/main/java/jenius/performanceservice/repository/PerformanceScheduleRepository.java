package jenius.performanceservice.repository;

import jenius.performanceservice.domain.PerformanceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PerformanceScheduleRepository extends JpaRepository<PerformanceSchedule, Long> {
    List<PerformanceSchedule> findAllByPerformanceId(Long performanceId);
    Optional<PerformanceSchedule> findByPerformanceIdAndPerformanceDate(Long performanceId, LocalDate performanceDate);
}
