package jenius.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"jenius.common", "jenius.userservice"})
public class UserServiceTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceTestApplication.class, args);
    }
}
