package jenius.payservice.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KakaoPayApproveRequestDto {
    private String tid;
    private String orderId;
    private String userId;
}
