package jenius.seatservice.exception;

import jenius.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum SeatErrorCode implements ErrorCode {

    ALREADY_CANCELED_RESERVATION(HttpStatus.BAD_REQUEST, "이미 취소된 좌석입니다."),
    ALREADY_RESERVED_RESERVATION(HttpStatus.BAD_REQUEST, "이미 예약된 좌석입니다.");

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
