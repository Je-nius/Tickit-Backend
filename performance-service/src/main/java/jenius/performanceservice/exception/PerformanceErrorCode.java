package jenius.performanceservice.exception;

import jenius.commonexception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PerformanceErrorCode implements ErrorCode {

    FULL_SEAT_EXCEPTION(HttpStatus.CONFLICT, "잔여 좌석이 없습니다."),
    INVALID_PERFORMANCE_SEAT(HttpStatus.BAD_REQUEST, "좌석 수는 0 이상이어야 합니다."),
    INVALID_PERFORMANCE_DATE(HttpStatus.BAD_REQUEST, "공연 시작일은 종료일보다 앞서야 힙니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
