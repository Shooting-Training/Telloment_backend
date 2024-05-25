package cau.capstone.backend.global.security;


import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";


    private static final List<String> EXCLUDE_URLS = List.of(
            "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/**", "/health",
            "/api/health/**",  "/api/auth/**", "/api/auth/signup", "/register/**", "/login/**","/swagger-ui/**","/swagger-resources",
            "/swagger-resources/**", "/swagger-ui.html", "/v3/api-docs/**", "index.html", "/s3/**"
    );


    private final JwtTokenProvider jwtTokenProvider;

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 1. Request Header 에서 토큰을 꺼냄
        String jwt = resolveToken(request);

        // 2. validateToken 으로 토큰 유효성 검사
        // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

//    // 현재 요청 URI 가 예외 URL 인지 검사
//    private boolean isExcludedUrl(String requestUri) {
//        return EXCLUDE_URLS.stream().anyMatch(requestUri::startsWith);
//    }

//    private boolean isExcludedUrl(String requestUri) {
//        return EXCLUDE_URLS.stream().anyMatch(excludeUrl -> {
//            try {
//                return new AntPathMatcher().match(excludeUrl, requestUri);
//            } catch (Exception e) {
//                return false;
//            }
//        });
//    }

}
