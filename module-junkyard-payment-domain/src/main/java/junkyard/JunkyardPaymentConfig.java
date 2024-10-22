package junkyard;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@PropertySource(encoding = "UTF-8", ignoreResourceNotFound = true,
        value = {
                "classpath:application-payment-domain-${spring.profiles.active}.properties"
        }
)
@Configuration
public class JunkyardPaymentConfig {
}
