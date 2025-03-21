package jenius.reservationservice.service;

import jenius.common.exception.CustomException;
import jenius.payservice.service.KakaoPayService;
import jenius.performanceservice.domain.Performance;
import jenius.performanceservice.service.PerformanceService;
import jenius.reservationservice.domain.Reservation;
import jenius.reservationservice.domain.ReservationStatus;
import jenius.reservationservice.dto.request.ReservationCancelRequestDto;
import jenius.reservationservice.dto.request.ReservationCreateRequestDto;
import jenius.reservationservice.dto.response.ReservationCancelResponseDto;
import jenius.reservationservice.dto.response.ReservationCreateResponseDto;
import jenius.reservationservice.repository.ReservationRepository;
import jenius.seatservice.domain.SeatType;
import jenius.ticketservice.domain.Ticket;
import jenius.ticketservice.service.TicketService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PerformanceService performanceService;

    @Mock
    private TicketService ticketService;

    @Mock
    private KakaoPayService kakaoPayService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("예매를 할 수 있다.")
    public void createReservation() {
        // given
        Long userId = 1L;
        Long performanceScheduleId = 1L;
        int quantity = 2;
        String performanceTitle = "뮤지컬 테스트";
        String reservationNumber = "T1234567890";
        LocalDate startDate = LocalDate.of(2025, 4, 2);
        LocalDate endDate = LocalDate.of(2025, 4, 3);

        ReservationCreateRequestDto reservationCreateRequestDto =
                ReservationCreateRequestDto.builder()
                        .performanceScheduleId(performanceScheduleId)
                        .seatType(SeatType.VIP)
                        .quantity(quantity)
                        .build();

        Performance mockPerformance = Performance.builder()
                .title(performanceTitle)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Reservation mockReservation = Reservation.builder()
                .userId(userId)
                .performanceScheduleId(performanceScheduleId)
                .reservationNumber(reservationNumber)
                .build();

        List<Ticket> mockTickets = List.of(
                new Ticket(1L, 1L),
                new Ticket(1L, 2L)
        );

        Long totalAmount = 20000L;

        // when
        when(performanceService.findPerformanceByScheduleId(performanceScheduleId))
                .thenReturn(mockPerformance);

        when(reservationRepository.save(any()))
                .thenReturn(mockReservation);

        when(ticketService.createTickets(any(), any(), any(), anyInt()))
                .thenReturn(mockTickets);

        when(ticketService.getTicketPrice(any())).thenReturn(10000L);

        // then
        ReservationCreateResponseDto createResponseDto =
                reservationService.reserve(userId, reservationCreateRequestDto);

        Assertions.assertNotNull(createResponseDto.getReservationNumber());
        Assertions.assertEquals(ReservationStatus.RESERVED, createResponseDto.getReservationStatus());
        Assertions.assertEquals(totalAmount, createResponseDto.getTotalAmount());
    }

    @Test
    @DisplayName("예매를 취소할 수 있다.")
    public void cancelReservation() {
        // given
        Long userId = 1L;
        Long reservationId = 1L;

        Reservation reservation = Reservation.builder()
                .userId(userId)
                .status(ReservationStatus.RESERVED)
                .build();

        // TODO: 결제 서비스가 추가 시, 결제 데이터도 필요

        ReservationCancelRequestDto reservationCancelRequestDto =
                ReservationCancelRequestDto.builder()
                        .reservationId(reservationId)
                        .build();

        given(reservationRepository.findReservationById(reservationId))
                .willReturn(Optional.ofNullable(reservation));

        // when
        ReservationCancelResponseDto reservationCancelResponseDto =
                reservationService.cancelReservation(userId, reservationCancelRequestDto);

        // then
        Assertions.assertEquals(ReservationStatus.CANCELED,
                reservationCancelResponseDto.getReservationStatus());
    }

    @Test
    @DisplayName("본인 예매가 아닌 경우 예매 취소 실패 테스트")
    public void cancelReservation_fail_notOwner() {
        // given
        Long userId = 1L;
        Long otherUserId = 2L;
        Long reservationId = 1L;

        Reservation reservation = Reservation.builder()
                .userId(otherUserId)
                .build();

        ReservationCancelRequestDto reservationCancelRequestDto =
                ReservationCancelRequestDto.builder()
                        .reservationId(reservationId)
                        .build();

        given(reservationRepository.findReservationById(reservationId))
                .willReturn(Optional.ofNullable(reservation));

        // when & then
        Assertions.assertThrows(CustomException.class,
                () -> reservationService.cancelReservation(userId, reservationCancelRequestDto),
                "본인 예매만 취소할 수 있습니다.");
    }

    @Test
    @DisplayName("이미 취소된 예매인 경우 예매 취소 실패 테스트")
    public void cancelReservation_fail_alreadyCanceled() {
        // given
        Long userId = 1L;
        Long reservationId = 1L;

        Reservation reservation = Reservation.builder()
                .userId(userId)
                .build();

        reservation.cancel(); // 취소

        ReservationCancelRequestDto reservationCancelRequestDto =
                ReservationCancelRequestDto.builder()
                        .reservationId(reservationId)
                        .build();

        given(reservationRepository.findReservationById(reservationId))
                .willReturn(Optional.of(reservation));

        // when & then
        Assertions.assertThrows(CustomException.class,
                () -> reservationService.cancelReservation(userId, reservationCancelRequestDto),
                "이미 취소된 예매입니다.");
    }

}
