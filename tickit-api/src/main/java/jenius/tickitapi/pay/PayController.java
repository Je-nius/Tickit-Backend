package jenius.tickitapi.pay;

import jenius.payservice.dto.request.KakaoPayReadyRequestDto;
import jenius.payservice.dto.response.KakaoPayApproveResponseDto;
import jenius.payservice.dto.response.KakaoPayReadyResponseDto;
import jenius.payservice.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/ready")
    public ResponseEntity<KakaoPayReadyResponseDto> readyForKakaoPay(@RequestBody KakaoPayReadyRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(kakaoPayService.readyForKakaoPay(requestDto));
    }

    @GetMapping("/approve")
    public ResponseEntity<KakaoPayApproveResponseDto> approveForKakaoPay(@RequestParam(name = "order_id") String orderId,
                                                                         @RequestParam(name = "user_id") String userId,
                                                                         @RequestParam(name = "pg_token") String pgToken) {
        return ResponseEntity.ok()
                .body(kakaoPayService.approveForKakaoPay(pgToken, orderId, userId));
    }

    @PostMapping("/cancel")
    public ResponseEntity<KakaoPayReadyResponseDto> cancelForKakaoPay(@RequestBody KakaoPayReadyRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(kakaoPayService.readyForKakaoPay(requestDto));
    }

    @GetMapping("/success")
    public String successReady() {
        return "ready success";
    }

    @GetMapping("/cancel")
    public String cancelReady() {
        return "cancel ready";
    }

    @GetMapping("/fail")
    public String failReady() {
        return "fail ready";
    }

}
