package jenius.userservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginRequestDto {
    private String code;
    private String error;
    private String error_description;
    private String state;
}
