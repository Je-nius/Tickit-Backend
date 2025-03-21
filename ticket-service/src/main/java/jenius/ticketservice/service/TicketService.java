package jenius.ticketservice.service;

import jenius.common.exception.CustomException;
import jenius.seatservice.domain.Seat;
import jenius.seatservice.domain.SeatType;
import jenius.seatservice.service.SeatService;
import jenius.ticketservice.domain.Ticket;
import jenius.ticketservice.exception.TicketErrorCode;
import jenius.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final SeatService seatService;

    public List<Ticket> createTickets(Long performanceScheduleId, SeatType seatType,
                              Long reservationId, int quantity) {

        List<Ticket> tickets = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            Seat seat = seatService.findFirstAvailableSeat(performanceScheduleId, seatType);
            Ticket ticket = Ticket.builder()
                    .reservationId(reservationId)
                    .seatId(seat.getId())
                    .build();
            tickets.add(ticket);
        }

        return ticketRepository.saveAll(tickets);
    }

    public Long getTicketPrice(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new CustomException(TicketErrorCode.NOT_FOUND_TICKET));

        return seatService.getSeatPrice(ticket.getSeatId());
    }


}

