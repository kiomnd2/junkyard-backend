package junkyard;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

@PropertySource(encoding = "UTF-8", ignoreResourceNotFound = true,
        value = {
                "classpath:core-web-${spring.profiles.active}.properties"
        }
)
@Configuration
public class JunkyardWebConfig {
}
