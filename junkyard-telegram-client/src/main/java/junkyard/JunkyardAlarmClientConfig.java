package junkyard;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(encoding = "UTF-8", ignoreResourceNotFound = true,
        value = {
                "classpath:alarm-client.properties",
                "classpath:alarm-client-${spring.profiles.active}.properties"
        }
)
public class JunkyardAlarmClientConfig {

}
