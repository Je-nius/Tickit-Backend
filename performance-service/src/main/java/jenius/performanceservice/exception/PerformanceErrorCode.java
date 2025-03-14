package jenius.performanceservice.exception;

import jenius.commonexception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PerformanceErrorCode implements ErrorCode {

    FULL_SEAT_EXCEPTION(HttpStatus.CONFLICT, "잔여 좌석이 없습니다."),
    INVALID_PERFORMANCE_SEAT(HttpStatus.BAD_REQUEST, "좌석 수는 0 이상이어야 합니다."),
    INVALID_PERFORMANCE_PERIOD(HttpStatus.BAD_REQUEST, "공연 시작일은 종료일보다 앞서야 힙니다."),
    INVALID_PERFORMANCE_DATE(HttpStatus.BAD_REQUEST, "공연 일자는 공연 기간 내에 있어야합니다."),
    INVALID_PERFORMANCE_INFORMATION_NUMBER(HttpStatus.BAD_REQUEST,
            "공연 기간에 맞는 날짜별 정보가 모두 입력되지 않았습니다.");

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
