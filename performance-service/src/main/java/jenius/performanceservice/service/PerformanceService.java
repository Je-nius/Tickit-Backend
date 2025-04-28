package jenius.performanceservice.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jenius.common.exception.CustomException;
import jenius.performanceservice.domain.*;
import jenius.performanceservice.dto.request.*;
import jenius.performanceservice.dto.response.PerformanceGenreSearchResponseDto;
import jenius.performanceservice.dto.response.PerformanceSearchResponseDto;
import jenius.performanceservice.dto.response.PerformanceCreateResponseDto;
import jenius.performanceservice.exception.PerformanceErrorCode;
import jenius.performanceservice.repository.PerformanceScheduleRepository;
import jenius.performanceservice.repository.PerformanceRepository;
import jenius.seatservice.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceScheduleRepository performanceScheduleRepository;
    private final SeatService seatService;
    private final ImageService imageService;

    @Transactional
    public PerformanceCreateResponseDto createPerformance(MultipartFile multipartFile,
                                                          PerformanceCreateRequestDto createRequestDto) throws IOException {

        Image image = imageService.createImage(multipartFile);

        // 공연 생성
        Performance performance = Performance.builder()
                .title(createRequestDto.getTitle())
                .startDate(createRequestDto.getStartDate())
                .endDate(createRequestDto.getEndDate())
                .runningTime(createRequestDto.getRunningTime())
                .genre(createRequestDto.getGenre())
                .location(createRequestDto.getLocation())
                .posterId(image.getId())
                .artists(createRequestDto.getArtists())
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
        List<PerformanceSchedule> performanceScheduleList = performanceScheduleRepository.saveAll(performanceSchedules);

        // 공연 일정 별 좌석 생성 & 전체 좌석 수 조회
        Map<LocalDate, Long> totalSeatNumber = new HashMap<>();

        for (PerformanceSchedule schedule : performanceSchedules) {
            seatService.generateSeatsForPerformanceSchedule(schedule.getId(), createRequestDto.getSeatConfig());
            Long seatCount = seatService.countAvailableSeat(schedule.getId());
            totalSeatNumber.put(schedule.getPerformanceDate(), seatCount);
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

        return PerformanceCreateResponseDto.fromEntity(performance, performanceScheduleList, totalSeatNumber);
    }

    /**
     * 공연 일정 ID 를 통해 공연 엔티티 가져오기
     */
    public Performance findPerformanceByScheduleId(Long performanceScheduleId) {
        PerformanceSchedule performanceSchedule = performanceScheduleRepository.findById(performanceScheduleId)
                .orElseThrow(() -> new CustomException(PerformanceErrorCode.NOT_FOUND_PERFORMANCE_SCHEDULE));

        return findPerformanceById(performanceSchedule.getPerformanceId());
    }

    /**
     * 공연 엔티티 가져오기
     *
     * @param performanceId
     * @return
     */
    public Performance findPerformanceById(Long performanceId) {
        return performanceRepository.findById(performanceId)
                .orElseThrow(() -> new CustomException(PerformanceErrorCode.NOT_FOUND_PERFORMANCE));
    }

    public List<PerformanceSearchResponseDto> searchPerformances(PerformanceSearchRequestDto searchRequestDto) {

        List<Performance> performances =
                performanceRepository.findPerformance(searchRequestDto.getKeyword());

        Map<Performance, String> performancePosters = getPerformancePosters(performances);

        return PerformanceSearchResponseDto.fromEntity(searchRequestDto.getKeyword(), performancePosters);
    }

    public List<PerformanceGenreSearchResponseDto> searchPerformancesByGenre(PerformanceGenreSearchRequestDto
                                                                                genreSearchRequestDto) {

        List<Performance> performances =
                performanceRepository.findPerformanceByGenre(genreSearchRequestDto.getGenre());

        Map<Performance, String> performancePosters = getPerformancePosters(performances);

        return PerformanceGenreSearchResponseDto.fromEntity(genreSearchRequestDto.getGenre(), performancePosters);
    }

    private Map<Performance, String> getPerformancePosters(List<Performance> performances) {
        Map<Performance, String> performancePosters = new HashMap<>();

        for (Performance performance : performances) {
            Image poster = imageService.findImageById(performance.getPosterId());
            String posterUrl = imageService.getFileURL(poster.getSavedFileName());
            performancePosters.put(performance, posterUrl);
        }
        return performancePosters;
    }

    /*
    public PerformanceUpdateResponseDto updatePerformance(PerformanceUpdateRequestDto updateRequestDto) {



    }
     */

    @Transactional
    public void deletePerformance(PerformanceDeleteRequestDto deleteRequestDto) {
        performanceRepository.deleteById(deleteRequestDto.getPerformanceId());
    }

}
