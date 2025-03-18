package jenius.payservice.exception;

import jenius.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum PaymentErrorCode implements ErrorCode {

    NOT_EXIST_PAY_INFORMATION(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
