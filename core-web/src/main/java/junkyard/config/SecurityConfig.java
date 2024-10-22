package junkyard.config;

import jakarta.servlet.DispatcherType;
import junkyard.filter.JwtAuthenticationEntryPoint;
import junkyard.filter.JwtFilter;
import junkyard.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception{
        HttpSecurity security = http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(httpSecurityHeadersConfigurer ->
                        httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                    auth.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR).permitAll();
                    auth.requestMatchers(
                            "/docs/index.html"
                            ,"/favicon.ico"
                            ,"/h2-console"
                                            ,"/v1/api/member/kakao/auth-check"
                                            ,"/v1/api/member/kakao/checkout"
                                            ,"/kakao/callback"
                                            ,"/v1/api/member/join"
                                            ,"/v1/api/member/refresh-token",
                            "/payment"
                            ).permitAll()
                                    .anyRequest().permitAll();
                        }
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtFilter(jwtTokenProvider, userDetailsService), UsernamePasswordAuthenticationFilter.class);
        return security.build();
    }


}
