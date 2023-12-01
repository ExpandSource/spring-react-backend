package com.busanit.springreactbackend.config;

import com.busanit.springreactbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;

    private final MemberService memberService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 요청 인가 설정
        http.authorizeHttpRequests((requests) -> requests
                // 프리플라이트 요청에 대한 허용
                //.requestMatchers(antMatcher(HttpMethod.OPTIONS, "/**")).permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .requestMatchers(antMatcher("/authenticate")).permitAll()
                .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                .requestMatchers(antMatcher("/api/member")).permitAll()
                .anyRequest().authenticated());

        // 별도로 설정한 CORS 필터 사용
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // HTTP 기본 인증 사용
        //http.httpBasic(Customizer.withDefaults());

        // 세션 사용하지 않음 (상태없는 REST API 사용)
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // CSRF 필터 사용하지 않음
        http.csrf((csrf) -> csrf.disable());

        // H2 Console 확인용 frame 사용
        http.headers((headers) -> headers.frameOptions((frame) -> frame.sameOrigin()));

        // OAuth 2.0 리소스 서버 사용 : JWT 요청 유효성 검사 기본 설정
        http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();

    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        //config.addAllowedOrigin("http://localhost:3000"); //
        config.addAllowedOrigin(frontendUrl); // 허가할 프론트엔드 url
        config.addAllowedMethod("*"); // 모든 메소드 허용.
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 인증 관리자 커스터마이징
    // (인증 요청이 발생하면 인증관리자는 하나 이상의 인증제공자에게 인증 프로세스를 위임)
    @Bean
    public AuthenticationManager authenticationManager(MemberService memberService, PasswordEncoder passwordEncoder) {

        // AuthenticationManager : 스프링 시큐리티에서 인증 요청을 담당하는 관리자
        // AuthenticationProvider : Manager는 제공자에게 인증프로세스를 위임
        // DaoAuthenticationProvider : 인터페이스 구현체, 패스워드인코더, 유저서비스 등을 필드로 가지고 있음.
        // UserDetailsService : 사용자 관련 데이터 검색에 사용

        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(memberService);

        // ProviderManager : AuthenticationManager의 구현체
        return new ProviderManager(authenticationProvider);
    }
}