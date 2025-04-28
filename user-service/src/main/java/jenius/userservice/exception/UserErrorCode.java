package jenius.userservice.exception;

import jenius.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

    DUPLICATED_LOGIN_ID(HttpStatus.CONFLICT, "중복된 ID 입니다."),
    NOT_LOGIN(HttpStatus.UNAUTHORIZED, "로그인 후 다시 이용해주세요."),
    LOGIN_ERROR(HttpStatus.UNAUTHORIZED, "로그인 중 오류가 발생했습니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
    NOT_FOUND_USER_BY_LOGIN_ID(HttpStatus.NOT_FOUND, "해당 ID 의 유저를 찾을 수 없습니다.");

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
