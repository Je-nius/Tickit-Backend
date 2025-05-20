package jenius.tickitapi.pay;

import jenius.payservice.dto.request.KakaoPayReadyRequestDto;
import jenius.payservice.dto.response.KakaoPayApproveResponseDto;
import jenius.payservice.dto.response.KakaoPayReadyResponseDto;
import jenius.payservice.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/api/ticket/pay")
    public ResponseEntity<KakaoPayReadyResponseDto> readyForKakaoPay(@RequestBody KakaoPayReadyRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(kakaoPayService.readyForKakaPay(requestDto));
    }

    @GetMapping("/api/pay/approve")
    public ResponseEntity<KakaoPayApproveResponseDto> approveForKakaoPay(@RequestParam(name = "order_id") String orderId,
                                                                         @RequestParam(name = "user_id") String userId,
                                                                         @RequestParam(name = "pg_token") String pgToken) {
        return ResponseEntity.ok()
                .body(kakaoPayService.approveForKakaoPay(pgToken, orderId, userId));
    }

    /* TODO
    @PostMapping("/cancel")
    public ResponseEntity<KakaoPayReadyResponseDto> cancelForKakaoPay(@RequestBody KakaoPayReadyRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(kakaoPayService.cancelForKakaoPay(requestDto));
    }
     */

}
