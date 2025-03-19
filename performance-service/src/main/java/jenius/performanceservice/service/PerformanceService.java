package jenius.performanceservice.service;

import jenius.common.exception.CustomException;
import jenius.performanceservice.domain.Performance;
import jenius.performanceservice.domain.PerformanceSchedule;
import jenius.performanceservice.dto.request.PerformanceDeleteRequestDto;
import jenius.performanceservice.dto.request.PerformanceScheduleDto;
import jenius.performanceservice.dto.request.PerformanceSearchRequestDto;
import jenius.performanceservice.dto.response.PerformanceSearchResponseDto;
import jenius.performanceservice.dto.request.PerformanceCreateRequestDto;
import jenius.performanceservice.dto.response.PerformanceCreateResponseDto;
import jenius.performanceservice.exception.PerformanceErrorCode;
import jenius.performanceservice.repository.PerformanceScheduleRepository;
import jenius.performanceservice.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceScheduleRepository performanceScheduleRepository;
    private final SeatService seatService;

    public PerformanceCreateResponseDto createPerformance(PerformanceCreateRequestDto createRequestDto) {

        // 공연 생성
        Performance performance = Performance.builder()
                .title(createRequestDto.getTitle())
                .startDate(createRequestDto.getStartDate())
                .endDate(createRequestDto.getEndDate())
                .runningTime(createRequestDto.getRunningTime())
                .genre(createRequestDto.getGenre())
                .location(createRequestDto.getLocation())
                .build();

        Performance savedPerformance = performanceRepository.save(performance);

        // 공연 일정 생성
        List<PerformanceSchedule> performanceSchedules = new ArrayList<>();

        for (PerformanceScheduleDto scheduleDto : createRequestDto.getSchedules()) {
            PerformanceSchedule schedule = PerformanceSchedule.builder()
                    .performanceId(savedPerformance.getId())
                    .performanceDate(scheduleDto.getPerformanceDate())
                    .startTime(scheduleDto.getStartTime())
                    .build();
            performanceSchedules.add(schedule);
        }
        performanceScheduleRepository.saveAll(performanceSchedules);


        // 공연 일정 별 좌석 생성
        for (PerformanceSchedule schedule : performanceSchedules) {
            seatService.generateSeatsForPerformanceSchedule(schedule);
        }

        // 공연 날짜 별 정보가 모두 들어왔는지 검증
        long dayOfPerformance = performance.getDayOfPerformance();

        if (createRequestDto.getSchedules().size() != dayOfPerformance) {
            throw new CustomException(PerformanceErrorCode.INVALID_PERFORMANCE_INFORMATION_NUMBER);
        }

        // 공연 날이 공연 기간 내에 존재하는지 검증
        List<PerformanceSchedule> schedules = createRequestDto.toEntity(savedPerformance.getId());
        for (PerformanceSchedule schedule : schedules) {
            schedule.validatePerformanceDate(savedPerformance.getStartDate(), savedPerformance.getEndDate());
        }

        List<PerformanceSchedule> performanceScheduleList = performanceScheduleRepository.saveAll(schedules);
        return PerformanceCreateResponseDto.fromEntity(performance, performanceScheduleList);
    }

    /**
     * 공연 엔티티 가져오기
     * @param performanceId
     * @return
     */
    public Performance findPerformanceById(Long performanceId) {
        return performanceRepository.findById(performanceId)
                .orElseThrow(() -> new CustomException(PerformanceErrorCode.NOT_FOUND_PERFORMANCE));
    }

    public List<PerformanceSearchResponseDto> searchPerformances(PerformanceSearchRequestDto searchRequestDto) {

        List<Performance> performances =
                performanceRepository.findByTitleOrLocation(searchRequestDto.getKeyword());

        return PerformanceSearchResponseDto.fromEntity(searchRequestDto.getKeyword(),
                performances);
    }

    /*
    public PerformanceUpdateResponseDto updatePerformance(PerformanceUpdateRequestDto updateRequestDto) {



    }
     */

    public void deletePerformance(PerformanceDeleteRequestDto deleteRequestDto) {
        performanceRepository.deleteById(deleteRequestDto.getPerformanceId());
    }

}
