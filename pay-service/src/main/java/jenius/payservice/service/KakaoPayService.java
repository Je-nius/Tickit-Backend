package jenius.payservice.service;

import jenius.common.config.KakaoProperties;
import jenius.common.exception.CustomException;
import jenius.payservice.domain.Payment;
import jenius.payservice.domain.PaymentStatus;
import jenius.payservice.dto.request.KakaoPayReadyRequestDto;
import jenius.payservice.dto.response.KakaoPayApproveResponseDto;
import jenius.payservice.dto.response.KakaoPayReadyResponseDto;
import jenius.payservice.exception.PaymentErrorCode;
import jenius.payservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoPayService {

    private final PaymentRepository paymentRepository;
    private final WebClient kakaoPayWebClient;
    private final KakaoProperties kakaoProperties;

    /**
     * 결제정보를 카카오페이 서버에 전달하고 결제 고유번호(TID)와 URL을 응답받는 단계
     */
    public KakaoPayReadyResponseDto readyForKakaoPay(KakaoPayReadyRequestDto readyRequestDto) {

        KakaoPayReadyResponseDto readyResponseDto = kakaoPayWebClient.post()
                .uri("/ready")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of(
                                "cid", kakaoProperties.getCid(),
                                "partner_order_id", readyRequestDto.getOrderId(),
                                "partner_user_id", readyRequestDto.getUserId(),
                                "item_name", readyRequestDto.getItemName(),
                                "quantity", readyRequestDto.getQuantity(),
                                "total_amount", readyRequestDto.getTotalAmount(),
                                "tax_free_amount", "0",
                                "approval_url", kakaoProperties.getApprovalUrl()
                                        + "?order_id=" + readyRequestDto.getOrderId() + "&user_id=" + readyRequestDto.getUserId(),
                                "cancel_url", kakaoProperties.getCancelUrl(),
                                "fail_url", kakaoProperties.getFailUrl()
                        ))
                )
                .retrieve()
                .bodyToMono(KakaoPayReadyResponseDto.class)
                .block();

        Payment payment = Payment.builder()
                .tid(readyResponseDto.getTid())
                .orderId(readyRequestDto.getOrderId())
                .userId(readyRequestDto.getUserId())
                .status(PaymentStatus.READY)
                .build();

        paymentRepository.save(payment);
        return readyResponseDto;
    }

    public KakaoPayApproveResponseDto approveForKakaoPay(String pgToken, String orderId, String userId) {
        Payment findPayment = paymentRepository.findByOrderIdAndUserIdAndStatus(orderId, userId, PaymentStatus.READY)
                .orElseThrow(() -> new CustomException(PaymentErrorCode.NOT_EXIST_PAY_INFORMATION));

        return kakaoPayWebClient.post()
                .uri("/approve")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of(
                                "cid", kakaoProperties.getCid(),
                                "tid", findPayment.getTid(),
                                "partner_order_id", findPayment.getOrderId(),
                                "partner_user_id", findPayment.getUserId(),
                                "pg_token", pgToken
                        ))
                )
                .retrieve()
                .bodyToMono(KakaoPayApproveResponseDto.class)
                .block();
    }
}
