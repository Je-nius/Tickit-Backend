package jenius.performanceservice.service;

import jenius.common.exception.CustomException;
import jenius.performanceservice.domain.PerformanceSchedule;
import jenius.performanceservice.exception.PerformanceErrorCode;
import jenius.performanceservice.repository.PerformanceScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PerformanceScheduleService {

    private final PerformanceScheduleRepository performanceScheduleRepository;

    public PerformanceSchedule findByPerformanceIdAndPerformanceDate(Long performanceId, LocalDate performanceDate) {
        return performanceScheduleRepository.findByPerformanceIdAndPerformanceDate(performanceId, performanceDate)
                .orElseThrow(() -> new CustomException(PerformanceErrorCode.NOT_FOUND_PERFORMANCE_SCHEDULE));
    }

}
