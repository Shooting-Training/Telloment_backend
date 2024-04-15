package cau.capstone.backend.security.entity;



import cau.capstone.backend.model.DTO.CustomUserInfoDto;
import cau.capstone.backend.model.entity.Authority;
import cau.capstone.backend.security.service.CustomUserDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtProvider {


    private final Key key;
    private final long accessTokenExpTime;


    public JwtProvider(
            @Value("${jwt.secret.key}") String secretKey,
            @Value("${jwt.expiration_time}") long accessTokenExpTime
    ){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    public String createAccessToken(CustomUserInfoDto userInfo) {
        return createToken(userInfo, accessTokenExpTime);
    }

    private String createToken(CustomUserInfoDto userInfo, long expTime) {
        Claims claims = Jwts.claims();
        claims.put("userId", userInfo.getUserId());
        claims.put("email", userInfo.getEmail());
        claims.put("role", userInfo.getAuthorities());

        ZonedDateTime now  = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public Long getUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }


    public boolean validateToken(String token) {

        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("Invalid JWT Token");
        } catch (ExpiredJwtException e){
            log.info("Expired JWT Token");
        } catch (UnsupportedJwtException e){
            log.info("Unsupported JWT Token");
        } catch (IllegalArgumentException e){
            log.info("JWT claims string is empty");
        }
        return false;

    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token");
            return e.getClaims();
        }
    }
}

