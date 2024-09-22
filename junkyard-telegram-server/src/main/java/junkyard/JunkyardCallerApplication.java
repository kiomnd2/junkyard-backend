package junkyard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ConfigurationPropertiesScan
@PropertySource(encoding = "UTF-8", ignoreResourceNotFound = true,
        value = {
                "classpath:application-alarm-${spring.profiles.active}.properties"
        })
public class JunkyardCallerApplication {

    public static void main(String[] args){
        SpringApplication.run(JunkyardCallerApplication.class);
    }
}
