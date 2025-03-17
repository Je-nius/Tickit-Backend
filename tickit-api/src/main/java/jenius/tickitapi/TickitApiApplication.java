package jenius.tickitapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "jenius")
@EnableJpaRepositories(basePackages = "jenius")
@EntityScan(basePackages = "jenius")
public class TickitApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TickitApiApplication.class, args);
	}

}
