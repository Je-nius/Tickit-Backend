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
import jenius.reservationservice.repository.ReservationRepository;
import jenius.seatservice.domain.SeatType;
import jenius.seatservice.dto.request.SeatInfoDto;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PerformanceService performanceService;

    @Mock
    private KakaoPayService kakaoPayService;

    @Mock
    private CreateReservationService createReservationService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("예매를 할 수 있다.")
    public void createReservation() {
        // given
        String userId = "tsetId";
        Long performanceId = 1L;
        Long performanceScheduleId = 1L;
        int quantity = 2;
        Long totalAmount = 169_000L;
        String performanceTitle = "뮤지컬 테스트";
        String reservationNumber = "T1234567890";
        LocalDate startDate = LocalDate.of(2025, 4, 2);
        LocalDate endDate = LocalDate.of(2025, 4, 3);
        LocalDate reservationDate = LocalDate.of(2025, 4, 2);

        List<SeatInfoDto> seatInfoDtos = List.of(
                SeatInfoDto.builder()
                        .seatType(SeatType.VIP)
                        .quantity(quantity)
                        .build()
        );

        ReservationCreateRequestDto reservationCreateRequestDto =
                ReservationCreateRequestDto.builder()
                        .performanceId(performanceId)
                        .performanceDate(reservationDate)
                        .seatInfos(seatInfoDtos)
                        .totalAmount(totalAmount)
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

        // when
        when(performanceService.findPerformanceByScheduleId(performanceScheduleId))
                .thenReturn(mockPerformance);

        when(createReservationService.createReservationAndTicket(userId, anyLong(),
                any(ReservationCreateRequestDto.class)))
                .thenReturn(mockReservation);

        when(kakaoPayService.readyForKakaPay(any(KakaoPayReadyRequestDto.class)))
                .thenReturn(null);

        // then
        ReservationCreateResponseDto createResponseDto =
                reservationService.reserve(userId, reservationCreateRequestDto);

        Assertions.assertNotNull(createResponseDto.getReservationNumber());
        Assertions.assertEquals(ReservationStatus.RESERVED, createResponseDto.getReservationStatus());
    }

    @Test
    @DisplayName("예매를 취소할 수 있다.")
    public void cancelReservation() {
        // given
        String userId = "tsetId";
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
        String userId = "tsetId";
        String otherUserId = "otherTestId";
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
        String userId = "tsetId";
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
