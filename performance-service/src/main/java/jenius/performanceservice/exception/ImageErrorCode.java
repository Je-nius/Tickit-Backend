package jenius.performanceservice.exception;

import jenius.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {

    NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND, "이미지 파일 없음");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
