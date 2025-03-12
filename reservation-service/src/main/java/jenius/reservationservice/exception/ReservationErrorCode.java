package jenius.reservationservice.exception;

import jenius.commonexception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ReservationErrorCode implements ErrorCode {

    EXCEED_MAX_QUANTITY(HttpStatus.BAD_REQUEST, "최대 수량을 초과하였습니다."),
    INVALID_RESERVATION_STATE(HttpStatus.BAD_REQUEST, "이미 취소된 예매입니다."),
    NOT_FOUND_RESERVATION(HttpStatus.NOT_FOUND, "존재하지 않는 예매 번호입니다."),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "본인 예매만 취소할 수 있습니다."),
    FAIL_GENERATE_RESERVATION_NUMBER(HttpStatus.CONFLICT, "예매 번호 생성에 실패했습니다. - 중복 번호");

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
