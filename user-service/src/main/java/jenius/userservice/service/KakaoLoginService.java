package jenius.userservice.service;

import jenius.common.config.KakaoProperties;
import jenius.userservice.dto.request.KakaoLoginRequestDto;
import jenius.userservice.dto.response.KakaoTokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    @Qualifier(value = "kakaoLoginWebClient")
    private final WebClient kakaoLoginWebClient;
    private final KakaoProperties kakaoProperties;

    public void authorize() {
        kakaoLoginWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/authorize")
                        .queryParam("client_id", kakaoProperties.getAppKey())
                        .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
                        .queryParam("response_type", "code")
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    public KakaoTokenResponseDto getToken(KakaoLoginRequestDto loginRequestDto) {

//        if (loginRequestDto.getError() != null) {
//
//        }

        return kakaoLoginWebClient.post()
                .uri("/oauth/token")
                .body(BodyInserters.fromValue(Map.of(
                        "grant_type", "authorization_code",
                        "client_id", kakaoProperties.getAppKey(),
                        "redirect_uri", kakaoProperties.getRedirectUri(),
                        "code", loginRequestDto.getCode()
                )))
                .retrieve()
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();
    }


}
