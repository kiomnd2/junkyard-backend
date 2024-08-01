package junkyard;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource(encoding = "UTF-8", ignoreResourceNotFound = true,
        value = {
                "classpath:application-member-domain-rdb-local.properties"
        }
)
@Configuration
public class MemberDomainConfig {
}
