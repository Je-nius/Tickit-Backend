package jenius.reservationservice.service;

import jenius.commonexception.CustomException;
import jenius.reservationservice.domain.Reservation;
import jenius.reservationservice.domain.ReservationStatus;
import jenius.reservationservice.dto.request.ReservationCreateRequestDto;
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
        return ReservationCreateResponseDto.toDto("임시타이틀", reservation);
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
