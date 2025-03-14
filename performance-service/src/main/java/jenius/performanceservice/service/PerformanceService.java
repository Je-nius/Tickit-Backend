package jenius.performanceservice.service;

import jenius.performanceservice.domain.Performance;
import jenius.performanceservice.domain.PerformanceInfo;
import jenius.performanceservice.dto.request.PerformanceCreateRequestDto;
import jenius.performanceservice.dto.request.PerformanceInfoDto;
import jenius.performanceservice.dto.response.PerformanceCreateResponseDto;
import jenius.performanceservice.repository.PerformanceInfoRepository;
import jenius.performanceservice.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceInfoRepository performanceInfoRepository;

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

        // 공연 일자 별 PerformanceSeat 생성
        long dayOfPerformance = performance.getDayOfPerformance();
        List<PerformanceInfo> infoList = new ArrayList<>();

        for (PerformanceInfoDto infoDto : createRequestDto.getInformation()) {
            PerformanceInfo performanceInfo = PerformanceInfo.builder()
                    .performanceId(savedPerformance.getId())
                    .performanceDate(infoDto.getPerformanceDate())
                    .startTime(infoDto.getStartTime())
                    .availableSeats(infoDto.getAvailableSeats())
                    .build();

            infoList.add(performanceInfo);
        }

        List<PerformanceInfo> performanceInfoList = performanceInfoRepository.saveAll(infoList);
        return PerformanceCreateResponseDto.fromEntity(performance, performanceInfoList);
    }
    
    

}
