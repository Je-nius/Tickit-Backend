package jenius.reservationservice.exception;

import jenius.commonexception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ReservationErrorCode implements ErrorCode {

    EXCEED_MAX_QUANTITY(HttpStatus.BAD_REQUEST, "최대 수량을 초과하였습니다."),
    FAIL_GENERATE_RESERVATION_NUMBER(HttpStatus.CONFLICT, "예약 번호 생성에 실패했습니다. - 중복 번호");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
