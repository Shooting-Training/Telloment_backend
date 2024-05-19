package cau.capstone.backend.global.security.config;

import cau.capstone.backend.global.security.JwtAuthFilter;
import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] AUTH_LIST = { // swagger 관련 URl
            "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/**", "/health"
    };

    private static final String[] AUTH_LIST2 = {
            "/api/auth/**","/register/**", "/login/**","/swagger-ui/**","/swagger-resources",
            "/swagger-resources/**", "/swagger-ui.html", "/v3/api-docs/**", "index.html", "/s3/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // 기본 인증 사용 안 함
                .csrf().disable() // CSRF 보호 사용 안 함
                .cors().configurationSource(corsConfigurationSource()) // CORS 설정
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 정책: STATELESS
                .and()
                .authorizeRequests() // 요청에 대한 사용 권한 체크
                .antMatchers(AUTH_LIST).permitAll() // 공개적으로 접근 가능한 URL
                .antMatchers(AUTH_LIST2).permitAll() // 공개적으로 접근 가능한 URL
                .antMatchers("/admin/**").hasRole("ADMIN") // ADMIN 권한만 접근 가능
                .antMatchers("/**").hasRole("USER") // 기본 USER 권한
                .anyRequest().denyAll()
                .and()
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler()) // 접근 거부 처리
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()); // 인증 실패 처리
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("*"));
            config.setAllowedMethods(List.of("*"));
            return config;
        };
    }

    // 접근 거부 핸들러
    public static class CustomAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"권한이 없는 사용자입니다.\"}");
        }
    }

    // 인증 실패 핸들러
    public static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"인증되지 않은 사용자입니다.\"}");
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers(AUTH_LIST).permitAll() // swagger 등 기타 URL은 인증 없이 접근 가능
//                .antMatchers("/api/auth/**").permitAll() // 회원가입/로그인 관련 URL은 인증 없이 접근 가능
//                .anyRequest().authenticated() // 나머지 모든 URL은 Jwt 인증 필요
//                .and()
//                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
}