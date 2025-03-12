package jenius.reservationservice.service;

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

}
