package jenius.global.errorCode;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ReservationErrorCode implements ErrorCode {

    EXCEED_MAX_QUANTITY(400, "최대 수량을 초과하였습니다.");

    private final int code;
    private final String message;

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getMessage() {
        return "";
    }
}
