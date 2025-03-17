package jenius.performanceservice.service;

import jenius.commonexception.CustomException;
import jenius.performanceservice.domain.Performance;
import jenius.performanceservice.domain.PerformanceInfo;
import jenius.performanceservice.dto.request.PerformanceDeleteRequestDto;
import jenius.performanceservice.dto.request.PerformanceSearchRequestDto;
import jenius.performanceservice.dto.request.PerformanceUpdateRequestDto;
import jenius.performanceservice.dto.response.PerformanceSearchResponseDto;
import jenius.performanceservice.dto.request.PerformanceCreateRequestDto;
import jenius.performanceservice.dto.response.PerformanceCreateResponseDto;
import jenius.performanceservice.dto.response.PerformanceUpdateResponseDto;
import jenius.performanceservice.exception.PerformanceErrorCode;
import jenius.performanceservice.repository.PerformanceInfoRepository;
import jenius.performanceservice.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        // 공연 날짜 별 정보가 모두 들어왔는지 검증
        if (createRequestDto.getInformation().size() != dayOfPerformance) {
            throw new CustomException(PerformanceErrorCode.INVALID_PERFORMANCE_INFORMATION_NUMBER);
        }

        // 공연 날이 공연 기간 내에 존재하는지 검증
        List<PerformanceInfo> infoList = createRequestDto.toEntity(savedPerformance.getId());
        for (PerformanceInfo info : infoList) {
            info.validatePerformanceDate(savedPerformance.getStartDate(), savedPerformance.getEndDate());
        }

        List<PerformanceInfo> performanceInfoList = performanceInfoRepository.saveAll(infoList);
        return PerformanceCreateResponseDto.fromEntity(performance, performanceInfoList);
    }

    public List<PerformanceSearchResponseDto> findPerformance(PerformanceSearchRequestDto searchRequestDto) {

        List<Performance> performances =
                performanceRepository.findByTitleOrLocation(searchRequestDto.getKeyword());

        return PerformanceSearchResponseDto.fromEntity(searchRequestDto.getKeyword(),
                performances);
    }

//    public PerformanceUpdateResponseDto updatePerformance(PerformanceUpdateRequestDto updateRequestDto) {
//
//
//
//    }

    public void deletePerformance(PerformanceDeleteRequestDto deleteRequestDto) {
        performanceRepository.deleteById(deleteRequestDto.getPerformanceId());
    }

}
