package cau.capstone.backend.security.filter;


import cau.capstone.backend.security.entity.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import cau.capstone.backend.security.service.CustomUserDetailService;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailService cutomUserDetailsService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        //jwt에 헤더가 있는 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            //JWt 유효성 검증
            if(jwtProvider.validateToken(jwt)){
                Long userId = jwtProvider.getUserId(jwt);

                //유저와 토큰 일치 시 userDetails 생성
                UserDetails userDetails = cutomUserDetailsService.loadUserByUsername(userId.toString());

                if (userDetails == null) {
                    throw new ServletException("유저 정보를 찾을 수 없습니다.");
                }
                else{
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }


            }

            filterChain.doFilter(request, response);
        }
    }
}
