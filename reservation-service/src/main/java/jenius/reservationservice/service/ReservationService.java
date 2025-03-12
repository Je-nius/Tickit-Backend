package jenius.reservationservice.service;

import jenius.commonexception.CustomException;
import jenius.reservationservice.domain.Reservation;
import jenius.reservationservice.domain.ReservationStatus;
import jenius.reservationservice.dto.request.ReservationCancelRequestDto;
import jenius.reservationservice.dto.request.ReservationCreateRequestDto;
import jenius.reservationservice.dto.response.ReservationCancelResponseDto;
import jenius.reservationservice.dto.response.ReservationCreateResponseDto;
import jenius.reservationservice.exception.ReservationErrorCode;
import jenius.reservationservice.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
//    private final PerformanceService performanceService;

    public ReservationCreateResponseDto createReservation(
            Long userId, ReservationCreateRequestDto reservationCreateRequestDto
    ) {

        // TODO: 결제 API 요청/응답
        /*
         * PAY TYPE
         * 카카오페이 KAKAO_PAY
         * 무통장입금 VBANK
         */

        // 예매 번호 생성
        String reservationNumber = generateUniqueReservationNumber();

        // 예매 생성
        Reservation reservation = Reservation.builder()
                .userId(userId)
                .performanceId(reservationCreateRequestDto.getPerformanceId())
                .reservationNumber(reservationNumber)
                .quantity(reservationCreateRequestDto.getQuantity())
                .build();

        reservation.reserve();

        // TODO: 공연 이름 가져오기 (PerformanceService)
//        Performance performance =
//                performanceService.findPerformanceById(reservationCreateRequestDto.getPerformanceId());

        reservationRepository.save(reservation);
        return ReservationCreateResponseDto.fromEntity("임시타이틀", reservation);
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

    private String generateUniqueReservationNumber() {

        int retryNumber = 5;

        for (int i = 0; i < retryNumber; i++) {
            String rsvNumber = generateReservationNumber();
            if (!reservationRepository.existByRsvNumber(rsvNumber)) {
                return rsvNumber;
            }
        }

        throw new CustomException(ReservationErrorCode.FAIL_GENERATE_RESERVATION_NUMBER);
    }

    /**
     * 예약 번호 생성기 (format: TI1234567890)
     */
    private String generateReservationNumber() {

        StringBuilder rsvNumber = new StringBuilder("TI");

        // 단순 Random 은 규칙적으로 난수가 생성되기 때문에 안전하지 않다.
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < 10; i++) {
            rsvNumber.append(secureRandom.nextInt());
        }

        return rsvNumber.toString();
    }


}
