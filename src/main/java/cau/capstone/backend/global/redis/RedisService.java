//package cau.capstone.backend.global.redis;
//
//import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.util.concurrent.TimeUnit;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class RedisService {
//
//    private final RedisTemplate<String, String> redisTemplate;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    // key-value = RefrshToken-Email
//    public void setRefreshToken(String refreshToken, String email, long expirationMinutes) {
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//
//        // 만료시간 이후 삭제
//        valueOperations.set(refreshToken, email, Duration.ofMinutes(expirationMinutes));
//        log.info("만료 시간, 분: {}", Duration.ofMinutes(expirationMinutes));
//    }
//
//    // AccessToken 로그아웃
//    public void setAccessTokenLogout(String accessToken, long expiration) {
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        valueOperations.set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
//        String expireFormatString = String.format("%d min, %d sec",
//                TimeUnit.MILLISECONDS.toMinutes(expiration),
//                TimeUnit.MILLISECONDS.toSeconds(expiration) -
//                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(expiration))
//        );
//        log.info("access token 만료 시간, {}", expireFormatString);
//    }
//
//    // get RefreshToken
//    public String getRefreshToken(String refreshToken) {
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        // RefreshToken 없으면 null 반환
//        return valueOperations.get(refreshToken);
//    }
//
//    // get AccessToken
//    public String getAccessToken(String accessToken) {
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        return valueOperations.get(accessToken);
//    }
//
//    // delete RefreshToken
//    public void deleteRefreshToken(String refreshToken) {
//        // delete 메서드 삭제 시 true 반환
//        redisTemplate.delete(refreshToken);
//    }
//}
