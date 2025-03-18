package jenius.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "kakao.api")
public class KakaoProperties {
    private String key;
    private String cid;
    private String approvalUrl;
    private String cancelUrl;
    private String failUrl;
}