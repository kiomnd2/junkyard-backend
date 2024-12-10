package junkyard;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@PropertySource(encoding = "UTF-8", ignoreResourceNotFound = true,
        value = {
                "classpath:application-member-domain-rdb.properties",
                "classpath:application-member-domain-rdb-${spring.profiles.active}.properties"
        }
)
@Configuration
public class MemberDomainConfig {
}
