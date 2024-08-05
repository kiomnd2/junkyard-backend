package junkyard.config;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final DispatcherServletAutoConfiguration dispatcherServletAutoConfiguration;

    public SecurityConfig(DispatcherServletAutoConfiguration dispatcherServletAutoConfiguration) {
        this.dispatcherServletAutoConfiguration = dispatcherServletAutoConfiguration;
    }

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception{
        HttpSecurity security = http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(httpSecurityHeadersConfigurer ->
                        httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/"
                        ,"/v1/api/member/kakao/auth-check"
                        ,"/kakao/callback"
                        ,"/docs/**"
                        ,"/v1/api/member/join"
                        ,"/h2-console/**").permitAll())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return security.build();

    }
}
