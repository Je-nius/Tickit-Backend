package jenius.ticketservice.exception;

import jenius.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum TicketErrorCode implements ErrorCode {

    NOT_FOUND_TICKET(HttpStatus.NOT_FOUND, "존재하지 않는 티켓입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return null;
    }

    @Override
    public String getMessage() {
        return "";
    }
}
