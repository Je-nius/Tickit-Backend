package jenius.reservationservice.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    @DisplayName("예매할 수 있다.")
    public void reserve() {
        // given
        Reservation reservation = new Reservation();

        // when
        reservation.reserve();

        // then
        Assertions.assertEquals(ReservationStatus.RESERVED, reservation.getStatus());
        Assertions.assertNotNull(reservation.getReservationDate());
    }

    @Test
    @DisplayName("예매를 취소할 수 있다.")
    public void cancel() {
        // given
        Reservation reservation = new Reservation();
        reservation.reserve();

        // when
        reservation.cancel();

        // then
        Assertions.assertEquals(ReservationStatus.CANCELED, reservation.getStatus());
        Assertions.assertNotNull(reservation.getCancelDate());
    }

    @Test
    @DisplayName("티켓 최대 수량을 넘지 않으면 구매할 수 있다.")
    public void validQuantity() {
        // given & when
        Reservation reservation = new Reservation();
        int quantity = 2;
        int maxQuantity = 2;

        // then
        Assertions.assertTrue(reservation.isValidQuantity(quantity, maxQuantity));
    }

    @Test
    @DisplayName("티켓 최대 구매 수량을 초과할 수 없다.")
    public void validQuantity_exceed() {
        // given & when
        Reservation reservation = new Reservation();
        int quantity = 3;
        int maxQuantity = 2;

        // then
        Assertions.assertFalse(reservation.isValidQuantity(quantity, maxQuantity));
    }

}
