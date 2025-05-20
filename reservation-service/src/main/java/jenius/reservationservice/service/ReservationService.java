package jenius.reservationservice.service;

import jenius.common.exception.CustomException;
import jenius.payservice.dto.request.KakaoPayReadyRequestDto;
import jenius.payservice.service.KakaoPayService;
import jenius.performanceservice.domain.Performance;
import jenius.performanceservice.domain.PerformanceSchedule;
import jenius.performanceservice.service.PerformanceScheduleService;
import jenius.performanceservice.service.PerformanceService;
import jenius.reservationservice.domain.Reservation;
import jenius.reservationservice.domain.ReservationStatus;
import jenius.reservationservice.dto.request.ReservationCancelRequestDto;
import jenius.reservationservice.dto.request.ReservationCreateRequestDto;
import jenius.reservationservice.dto.response.ReservationCancelResponseDto;
import jenius.reservationservice.dto.response.ReservationCreateResponseDto;
import jenius.reservationservice.exception.ReservationErrorCode;
import jenius.reservationservice.repository.ReservationRepository;
import jenius.seatservice.dto.request.SeatInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PerformanceService performanceService;
    private final PerformanceScheduleService performanceScheduleService;
    private final CreateReservationService createReservationService;
    private final KakaoPayService kakaoPayService;


    public ReservationCreateResponseDto reserve(
            Long userId, ReservationCreateRequestDto reservationCreateRequestDto
    ) {

        // 공연 이름 가져오기 & 공연 스케줄 id 가져오기
        Performance performance =
                performanceService.findPerformanceById(reservationCreateRequestDto.getPerformanceId());
        String performanceTitle = performance.getTitle();
        PerformanceSchedule performanceSchedule = performanceScheduleService.findByPerformanceIdAndPerformanceDate(performance.getId(),
                reservationCreateRequestDto.getPerformanceDate());

        // 예매 및 티켓 생성
        Reservation reservation =
                createReservationService.createReservationAndTicket(userId, performanceSchedule.getId(),
                        reservationCreateRequestDto);
        reservation.pending();

        // 티켓 총 매수
        int totalQuantity = reservationCreateRequestDto.getSeatInfos()
                .stream()
                .map(SeatInfoDto::getQuantity)
                .reduce(0, Integer::sum);

        // 결제 요청 (KAKAO_PAY)
        KakaoPayReadyRequestDto readyRequestDto = KakaoPayReadyRequestDto.builder()
                .orderId(reservation.getReservationNumber())
                .userId(userId)
                .itemName(performanceTitle)
                .quantity(totalQuantity)
                .totalAmount(reservation.getTotalAmount())
                .build();

        try {
            kakaoPayService.readyForKakaPay(readyRequestDto);
            reservation.reserve();
            return ReservationCreateResponseDto.fromEntity(performanceTitle, reservation);
        } catch (CustomException e) {
            throw e;
        }
    }

    public ReservationCancelResponseDto cancelReservation(
            Long userId, ReservationCancelRequestDto reservationCancelRequestDto
    ) {
        // TODO: 결제 서비스 추가 시, 결제 데이터 & 검증 필요

        // 1. 예매 정보 조회
        Reservation reservation = reservationRepository
                .findReservationById(reservationCancelRequestDto.getReservationId())
                .orElseThrow(() -> new CustomException(ReservationErrorCode.NOT_FOUND_RESERVATION));

        // 2. 사용자 소유 검증
        if (!reservation.getUserId().equals(userId)) {
            throw new CustomException(ReservationErrorCode.ACCESS_DENIED_EXCEPTION);
        }

        // 3. 예매 상태 검증
        if (reservation.getStatus().equals(ReservationStatus.CANCELED)) {
            throw new CustomException(ReservationErrorCode.INVALID_RESERVATION_STATE);
        }

        // 4. 취소
        reservation.cancel();

        return ReservationCancelResponseDto.fromEntity(reservation);
    }

}
