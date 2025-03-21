package jenius.payservice.exception;

import jenius.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum PaymentErrorCode implements ErrorCode {

    NOT_EXIST_PAY_INFORMATION(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."),
    KAKAO_PAY_APPROVE_FAILED(HttpStatus.BAD_REQUEST, "카카오페이 결제 승인이 실패했습니다."),
    KAKAO_PAY_READY_FAILED(HttpStatus.BAD_REQUEST, "카카오페이 결제 준비에 실패했습니다."),
    KAKAO_SERVER_NOT_AVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "카카오페이 서버와 연결할 수 없습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.");

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
