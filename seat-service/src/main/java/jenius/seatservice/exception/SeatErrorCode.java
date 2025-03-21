package jenius.seatservice.exception;

import jenius.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum SeatErrorCode implements ErrorCode {

    ALREADY_CANCELED_RESERVATION(HttpStatus.BAD_REQUEST, "이미 취소된 좌석입니다."),
    ALREADY_RESERVED_RESERVATION(HttpStatus.BAD_REQUEST, "이미 예약된 좌석입니다."),
    NOT_FOUND_SEAT(HttpStatus.NOT_FOUND, "존재하지 않는 좌석입니다."),
    NOT_FOUND_PERFORMANCE_SEAT(HttpStatus.NOT_FOUND, "해당 공연의 좌석이 존재하지 않습니다."),
    NOT_FOUND_SEAT_TYPE(HttpStatus.NOT_FOUND, "해당 유형의 좌석이 존재하지 않습니다.");

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
