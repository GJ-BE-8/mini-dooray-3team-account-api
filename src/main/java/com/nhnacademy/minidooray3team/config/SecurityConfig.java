package com.nhnacademy.minidooray3team.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화를 위한 BCryptPasswordEncoder
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register", "/css/**", "/js/**").permitAll() // 인증 없이 접근 가능 경로
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자만 접근 가능
                        .requestMatchers("/member/**").hasRole("MEMBER") // 회원만 접근 가능
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // 커스텀 로그인 페이지 경로
                        .loginProcessingUrl("/auth/login/process") // 로그인 처리 URL
                        .defaultSuccessUrl("/home", true) // 로그인 성공 시 이동할 페이지
                        .failureUrl("/auth/login?error=true") // 로그인 실패 시 이동할 페이지
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout")) // 로그아웃 URL
                        .logoutSuccessUrl("/auth/login") // 로그아웃 성공 시 이동할 페이지
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .permitAll()
                );

        return http.build();
    }
}

