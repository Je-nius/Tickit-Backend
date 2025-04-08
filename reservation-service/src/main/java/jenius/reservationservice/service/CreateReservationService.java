package jenius.reservationservice.service;

import jenius.common.exception.CustomException;
import jenius.reservationservice.domain.Reservation;
import jenius.reservationservice.dto.request.ReservationCreateRequestDto;
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
public class CreateReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketService ticketService;

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
                .quantity(reservationCreateRequestDto.getQuantity())
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
