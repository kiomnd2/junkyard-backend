package junkyard;

import junkyard.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(SecurityConfig.class)
@SpringBootApplication
public class JunkyardApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JunkyardApiApplication.class);
    }
}
