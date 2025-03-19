package jenius.seatservice.domain;

import jenius.common.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeatTest {

    @Test
    @DisplayName("좌석을 예매할 수 있다.")
    void countAvailableSeat() {

        // given
        Seat seatA1 = Seat.builder()
                .performanceScheduleId(1L)
                .seatNumber("A1")
                .seatType(SeatType.VIP)
                .price(140_000L)
                .build();

        Seat seatB1 = Seat.builder()
                .performanceScheduleId(1L)
                .seatNumber("B1")
                .seatType(SeatType.STANDARD)
                .price(100_000L)
                .build();

        // when
        seatA1.reserve();

        // then
        Assertions.assertThat(seatA1.isReserved()).isTrue();
        Assertions.assertThat(seatB1.isReserved()).isFalse();
    }

    @Test
    @DisplayName("이미 예약된 좌석은 예약할 수 없다.")
    void alreadyReservedSeat_Exception() {

        // given
        Seat seatA1 = Seat.builder()
                .performanceScheduleId(1L)
                .seatNumber("A1")
                .seatType(SeatType.VIP)
                .price(140_000L)
                .build();

        Seat seatB1 = Seat.builder()
                .performanceScheduleId(1L)
                .seatNumber("B1")
                .seatType(SeatType.STANDARD)
                .price(100_000L)
                .build();

        seatA1.reserve();

        // when & then
        Assertions.assertThatThrownBy(seatA1::reserve)
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 예약된 좌석입니다.");
    }

    @Test
    @DisplayName("이미 취소된 좌석은 취소할 수 없다.")
    void alreadyCanceledSeat_Exception() {

        // given
        Seat seatA1 = Seat.builder()
                .performanceScheduleId(1L)
                .seatNumber("A1")
                .seatType(SeatType.VIP)
                .price(140_000L)
                .build();

        Seat seatB1 = Seat.builder()
                .performanceScheduleId(1L)
                .seatNumber("B1")
                .seatType(SeatType.STANDARD)
                .price(100_000L)
                .build();

        seatA1.reserve();
        seatA1.cancel();

        // when & then
        Assertions.assertThatThrownBy(seatA1::cancel)
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 취소된 좌석입니다.");
    }

}