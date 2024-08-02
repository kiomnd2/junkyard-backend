package junkyard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource(encoding = "UTF-8", ignoreResourceNotFound = true,
value = {
        "classpath:application-junkyard-api-${spring.profiles.active}.properties"
})
@SpringBootApplication
public class JunkyardApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JunkyardApiApplication.class);
    }
}
