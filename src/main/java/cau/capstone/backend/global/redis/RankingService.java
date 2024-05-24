package cau.capstone.backend.global.redis;

import cau.capstone.backend.User.model.Category;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Api(tags = "5. Ranking")
@Service
public class RankingService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String getLikeKey(Category category) {
        return "page:like:" + category.name().toLowerCase();
    }

    private String getViewCountKey(Category category) {
        return "page:viewcount:" + category.name().toLowerCase();
    }

    // 좋아요 추가
    public void likePage(Long pageId, Category category) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(getLikeKey(category), pageId.toString(), 1);
    }

    // 좋아요 제거
    public void unlikePage(Long pageId, Category category) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(getLikeKey(category), pageId.toString(), -1);
    }

    // 카테고리별 상위 N개의 포스트 조회
    public Set<String> getTopPagesLike(String categoryCode, int topN) {
        Category category = Category.getByCode(categoryCode);
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.reverseRange(getLikeKey(category), 0, topN - 1);
    }

    // 페이지 조회수 증가, 카테고리 별 분류
    public void viewPage(Long pageId, Category category) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(getViewCountKey(category), pageId.toString(), 1);
    }


    // 카테고리별 상위 N개의 포스트 조회 (조회수 기준)
    public Set<String> getTopViewedPages(String categoryCode, int topN) {
        Category category = Category.getByCode(categoryCode);
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.reverseRange(getViewCountKey(category), 0, topN - 1);
    }

    // 특정 포스트의 카테고리 내 랭킹 조회
//    public Long getPageRank(Long pageId, String categoryCode) {
//        Category category = Category.getByCode(categoryCode);
//
//        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
//        return zSetOps.reverseRank(getRankingKey(category), pageId.toString());
//    }
//
//    // 특정 포스트의 카테고리 내 좋아요 수 조회
//    public Double getPageScore(Long pageId, String categoryCode) {
//        Category category = Category.getByCode(categoryCode);
//
//        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
//        return zSetOps.score(getRankingKey(category), pageId.toString());
//    }
}
