package jenius.commonexception;

import org.springframework.http.HttpStatus;

/**
 * 인터페이스를 통해
 * ErrorCode 규격 정하기
 */
public interface ErrorCode {

    HttpStatus getHttpStatus();
    String getMessage();

}
