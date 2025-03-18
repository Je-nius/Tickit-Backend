package jenius.tickitapi;

import jenius.common.config.KakaoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "jenius")
@EnableJpaRepositories(basePackages = "jenius")
@EntityScan(basePackages = "jenius")
@EnableConfigurationProperties(KakaoProperties.class)
public class TickitApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TickitApiApplication.class, args);
	}

}
