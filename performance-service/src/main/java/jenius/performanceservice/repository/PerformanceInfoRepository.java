package jenius.performanceservice.repository;

import jenius.performanceservice.domain.PerformanceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceInfoRepository extends JpaRepository<PerformanceInfo, Long> {
}
