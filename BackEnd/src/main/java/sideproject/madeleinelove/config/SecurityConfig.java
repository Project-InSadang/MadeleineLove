package sideproject.madeleinelove.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/oauth-login/admin").hasRole("ADMIN") // ADMIN 역할이 필요한 경로
                        .requestMatchers("/oauth-login/info").hasAnyRole("USER", "ADMIN") // USER 또는 ADMIN 역할이 필요한 경로
                        .anyRequest().permitAll() // 나머지 요청은 모두 허용
                )
                .oauth2Login((oauth2) -> oauth2
                        .loginPage("/oauth-login/login") // 로그인 페이지 설정
                        .defaultSuccessUrl("/loginSuccess", true) // 로그인 성공 시 리다이렉트 URL
                        .failureUrl("/oauth-login/login?error=true") // 로그인 실패 시 리다이렉트 URL
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/oauth-login/logout")
                        .logoutSuccessUrl("/oauth-login/login?logout=true")
                )
                .csrf((csrf) -> csrf.disable()); // CSRF 비활성화

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}