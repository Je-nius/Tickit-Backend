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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoPayService {

    private final PaymentRepository paymentRepository;

    @Qualifier(value = "kakaoPayWebClient")
    private final WebClient kakaoPayWebClient;
    private final KakaoProperties kakaoProperties;

    /**
     * 결제정보를 카카오페이 서버에 전달하고 결제 고유번호(TID)와 URL을 응답받는 단계
     */
    public KakaoPayReadyResponseDto readyForKakaPay(KakaoPayReadyRequestDto readyRequestDto) {

        try {
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
        } catch (WebClientResponseException e) {
            log.error("[KakaoPay Ready 실패] 상태코드={}, 응답={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new CustomException(PaymentErrorCode.KAKAO_PAY_READY_FAILED);
        } catch (WebClientRequestException e) {
            log.error("[KakaoPay Ready 요청 실패] 네트워크 오류: {}", e.getMessage());
            throw new CustomException(PaymentErrorCode.KAKAO_SERVER_NOT_AVAILABLE);
        } catch (Exception e) {
            log.error("[KakaoPay Ready 알 수 없는 오류] {}", e.getMessage(), e);
            throw new CustomException(PaymentErrorCode.INTERNAL_ERROR);
        }
    }

    public KakaoPayApproveResponseDto approveForKakaoPay(String pgToken, String orderId, String userId) {
        Payment findPayment = paymentRepository.findByOrderIdAndUserIdAndStatus(orderId, userId, PaymentStatus.READY)
                .orElseThrow(() -> new CustomException(PaymentErrorCode.NOT_EXIST_PAY_INFORMATION));

        try {
            KakaoPayApproveResponseDto approveResponseDto = kakaoPayWebClient.post()
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

            findPayment.approve();
            return approveResponseDto;
        } catch (WebClientResponseException e) {
            // Kakao 서버에서 응답은 왔지만 상태코드가 4xx or 5xx
            findPayment.fail();
            log.error("[KakaoPay Approve 실패] 상태코드={}, 메시지={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new CustomException(PaymentErrorCode.KAKAO_PAY_APPROVE_FAILED);
        } catch (WebClientRequestException e) {
            // 네트워크 오류
            findPayment.fail();
            log.error("[KakaoPay Approve 요청 실패] 네트워크 문제: {}", e.getMessage());
            throw new CustomException(PaymentErrorCode.KAKAO_SERVER_NOT_AVAILABLE);
        } catch (Exception e) {
            // 기타 예상치 못한 예외
            findPayment.fail();
            log.error("[KakaoPay Approve 알 수 없는 오류] {}", e.getMessage(), e);
            throw new CustomException(PaymentErrorCode.INTERNAL_ERROR);
        }
    }
}
