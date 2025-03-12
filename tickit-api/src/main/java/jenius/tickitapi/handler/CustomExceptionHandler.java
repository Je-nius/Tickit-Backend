package jenius.tickitapi.handler;

import jenius.commonexception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {
        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(ex.getErrorCode().getMessage());
    }

}
