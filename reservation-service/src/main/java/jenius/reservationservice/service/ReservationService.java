package jenius.reservationservice.service;

import jenius.common.exception.CustomException;
import jenius.payservice.dto.request.KakaoPayReadyRequestDto;
import jenius.payservice.service.KakaoPayService;
import jenius.performanceservice.domain.Performance;
import jenius.performanceservice.service.PerformanceService;
import jenius.reservationservice.domain.Reservation;
import jenius.reservationservice.domain.ReservationStatus;
import jenius.reservationservice.dto.request.ReservationCancelRequestDto;
import jenius.reservationservice.dto.request.ReservationCreateRequestDto;
import jenius.reservationservice.dto.response.ReservationCancelResponseDto;
import jenius.reservationservice.dto.response.ReservationCreateResponseDto;
import jenius.reservationservice.exception.ReservationErrorCode;
import jenius.reservationservice.repository.ReservationRepository;
import jenius.ticketservice.domain.Ticket;
import jenius.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PerformanceService performanceService;
    private final TicketService ticketService;
    private final KakaoPayService kakaoPayService;


    public ReservationCreateResponseDto reserve(
            Long userId, ReservationCreateRequestDto reservationCreateRequestDto
    ) {

        // 공연 이름 가져오기
        Performance performance =
                performanceService.findPerformanceByScheduleId(reservationCreateRequestDto.getPerformanceScheduleId());
        String performanceTitle = performance.getTitle();

        // 예매 및 티켓 생성
        Reservation reservation =
                createReservationAndTicket(userId, reservationCreateRequestDto);

        reservation.pending();

        // 결제 요청 (KAKAO_PAY)
        KakaoPayReadyRequestDto readyRequestDto = KakaoPayReadyRequestDto.builder()
                .orderId(reservation.getReservationNumber())
                .userId(userId)
                .itemName(performanceTitle)
                .quantity(reservationCreateRequestDto.getQuantity())
                .totalAmount(reservation.getTotalAmount())
                .build();

        try {
            kakaoPayService.readyForKakaoPay(readyRequestDto);
            reservation.reserve();
            return ReservationCreateResponseDto.fromEntity(performanceTitle, reservation);
        } catch (CustomException e) {
            throw e;
        }
    }

    @Transactional
    public Reservation createReservationAndTicket(Long userId,
                                                  ReservationCreateRequestDto reservationCreateRequestDto) {
        // 예매 번호 생성
        String reservationNumber = generateUniqueReservationNumber();

        // 예매 생성
        Reservation reservation = Reservation.builder()
                .userId(userId)
                .performanceScheduleId(reservationCreateRequestDto.getPerformanceScheduleId())
                .reservationNumber(reservationNumber)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        // quantity 만큼 ticket 생성
        List<Ticket> tickets = ticketService.createTickets(reservationCreateRequestDto.getPerformanceScheduleId(),
                reservationCreateRequestDto.getSeatType(),
                savedReservation.getId(),
                reservationCreateRequestDto.getQuantity());

        // 총 가격 계산
        Long totalAmount = getTotalAmount(tickets);
        savedReservation.assignTotalAmount(totalAmount);

        return savedReservation;
    }

    private Long getTotalAmount(List<Ticket> tickets) {
        Long totalAmount = 0L;
        for (Ticket ticket : tickets) {
            totalAmount += ticketService.getTicketPrice(ticket.getId());
        }
        return totalAmount;
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
