package jenius.common.config;

import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebClientConfig {

    private final KakaoProperties kakaoProperties;

    @Bean
    public WebClient kakaoPayWebClient() {
        return WebClient.builder()
                .baseUrl("https://open-api.kakaopay.com/online/v1/payment")
                .defaultRequest(request -> request
                        .header(HttpHeaders.AUTHORIZATION, "SECRET_KEY " + kakaoProperties.getKey())
                        .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON.toString()))
                .build();
    }

    @Bean
    public WebClient kakaoLoginWebClient() {
        return WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .build();
    }

}
