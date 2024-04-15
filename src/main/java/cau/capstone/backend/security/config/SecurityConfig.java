package cau.capstone.backend.security.config;

import cau.capstone.backend.security.CustomAccessDeniedHandler;
import cau.capstone.backend.security.CustomAuthenticationEntryPoint;
import cau.capstone.backend.security.entity.JwtProvider;
import cau.capstone.backend.security.filter.JwtAuthenticationFilter;

import cau.capstone.backend.security.service.CustomUserDetailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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



import java.io.IOException;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private JwtProvider jwtProvider;

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/user/**", "/swagger-ui/**", "/api-docs", "/api/v1/auth/**",
            "/swagger-ui-custom.html", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable());
        http.cors(Customizer.withDefaults());

        //세션 관리 상태 없음으로 구성, Spring Security는 세션을 생성하지 않음
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //Form 로그인 방식 사용하지 않음, Basic Http 인증 사용하지 않음
        http.formLogin((form)->form.disable());
        http.httpBasic(AbstractHttpConfigurer::disable);

        //jwtAuthFilter를 UsernamePasswordAuthenticationFilter 전에 넣어줌
        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
        );

        //권한 규칙 설정
        http.authorizeRequests(authorize -> authorize
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().permitAll());


        return http.build();

    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
//
//
//
//
//
//        http
//                // ID, Password 문자열을 Base64로 인코딩하여 전달하는 구조
//                .httpBasic().disable()
//    // 쿠키 기반이 아닌 JWT 기반이므로 사용하지 않음
//                .csrf().disable()
//    // CORS 설정
//                .cors(c -> {
//        CorsConfigurationSource source = request -> {
//            // Cors 허용 패턴
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowedOrigins(
//                    List.of("*")
//            );
//            config.setAllowedMethods(
//                    List.of("*")
//            );
//            return config;
//        };
//        c.configurationSource(source);
//    }
//                )
//                        // Spring Security 세션 정책 : 세션을 생성 및 사용하지 않음
//                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//    // 조건별로 요청 허용/제한 설정
//                .authorizeRequests()
//    // 회원가입과 로그인은 모두 승인
//                .antMatchers("/register/**", "/login/**","/swagger-ui/**","/swagger-resources",
//                                     "/swagger-resources/**", "/swagger-ui.html", "/v3/api-docs/**", "index.html", "/s3/**").permitAll()
//    // /admin으로 시작하는 요청은 ADMIN 권한이 있는 유저에게만 허용
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/**").hasRole("USER")
//                .anyRequest().denyAll()
//                .and()
//    // JWT 인증 필터 적용
//                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
//            // 에러 핸들링
//            .exceptionHandling()
//                .accessDeniedHandler(new AccessDeniedHandler() {
//        @Override
//        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//            // 권한 문제가 발생했을 때 이 부분을 호출한다.
//            response.setStatus(403);
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("text/html; charset=UTF-8");
//            response.getWriter().write("권한이 없는 사용자입니다.");
//        }
//    })
//            .authenticationEntryPoint(new AuthenticationEntryPoint() {
//        @Override
//        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//            // 인증문제가 발생했을 때 이 부분을 호출한다.
//            response.setStatus(401);
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("text/html; charset=UTF-8");
//            response.getWriter().write("인증되지 않은 사용자입니다.");
//        }
//    });
//
//        return http.build();




//    @Autowired
//    private JwtFilter jwtFilter;

//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailService);
//    }
//
//    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//    @Override
//    protected AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()   // 보안에 관한 것
//                .authorizeRequests() //HttpServletRequest를 사용한 요정들에 대한 접근제한
//                .antMatchers("/authenticate",
//                "/fb/**",
//                "/register/**",
//                "/matching/**",
//                "/hello",
//                "/v3/api-docs/**",
//                "/swagger-ui/**",
//                "/v2/api-docs",
//                "/swagger-resources",
//                "/swagger-resources/**",
//                "/configuration/ui",
//                "/configuration/security",
//                "swagger-ui.html",
//                "/login",
//                "/email",
//                "/verify",
//                "/register",
//                "/delete/**",
//                "/upload",
//                "/accept",
//                "/reject",
//                "/profile/**"
//                ,"/ws-stomp/**"
//                ).permitAll()  //해당 url은 인증없이 접근 허용
//                .anyRequest().authenticated() //나머지 요청들은 인증되어야 한다.
//                .and()
//                .exceptionHandling()
//                .and()
//                .sessionManagement()    // 세션에 대해 관리
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//    @Bean
//    public JwtUtil jwtUtil() {
//        return new JwtUtil(secret);
//    }

}
