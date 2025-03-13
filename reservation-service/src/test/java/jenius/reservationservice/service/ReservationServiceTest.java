package jenius.reservationservice.service;

import jenius.commonexception.CustomException;
import jenius.reservationservice.domain.Reservation;
import jenius.reservationservice.domain.ReservationStatus;
import jenius.reservationservice.dto.request.ReservationCancelRequestDto;
import jenius.reservationservice.dto.request.ReservationCreateRequestDto;
import jenius.reservationservice.dto.response.ReservationCancelResponseDto;
import jenius.reservationservice.dto.response.ReservationCreateResponseDto;
import jenius.reservationservice.repository.ReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("예매를 할 수 있다.")
    public void createReservation() {
        // given
        Long userId = 1L;
        Long performanceId = 1L;
        int quantity = 2;

        ReservationCreateRequestDto reservationCreateRequestDto =
                ReservationCreateRequestDto.builder()
                        .performanceId(performanceId)
                        .quantity(quantity)
                        .build();

        // when
        ReservationCreateResponseDto reservationCreateResponseDto =
                reservationService.createReservation(userId, reservationCreateRequestDto);

        // then
        Assertions.assertNotNull(reservationCreateResponseDto.getReservationNumber());
        Assertions.assertEquals(ReservationStatus.RESERVED, reservationCreateResponseDto.getReservationStatus());
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
