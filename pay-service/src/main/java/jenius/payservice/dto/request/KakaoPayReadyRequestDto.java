package jenius.payservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoPayReadyRequestDto {
    private String orderId;
    private Long userId;
    private String itemName;
    private int quantity;
    private Long totalAmount;
}
