package cau.capstone.backend.global.redis;

import cau.capstone.backend.page.model.Category;
import cau.capstone.backend.page.model.Emotion;
import cau.capstone.backend.page.model.EmotionType;
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


    //페이지를 이모션 기반으로 랭킹
    private String getLikeKey(EmotionType emotion) {
        return "page:like:" + emotion.getCode().toLowerCase();
    }

    private String getViewCountKey(EmotionType emotion) {
        return "page:viewcount:" + emotion.getCode().toLowerCase();
    }


    //페이지를 태그 기반으로 랭킹
    private String getLikeKeyPageTag(String tag) {return "page:like:" + tag;}
    private String getViewCountKeyTag(String tag) {return "page:viewcount:" + tag;}


    //북을 카테고리 기반으로 랭킹
    private String getLikeKeyBook(Category category) {
        return "book:like:" + category.name().toLowerCase();
    }

    private String getViewCountKeyBook(Category category) {
        return "book:viewcount:" + category.name().toLowerCase();
    }


    public void likePage(Long pageId, EmotionType emotion){
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(getLikeKey(emotion), pageId.toString(), 1);
    }

    public void likePageTag(Long pageId, String tag){
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(getLikeKeyPageTag(tag), pageId.toString(), 1);
    }


    public void unlikePage(Long pageId, EmotionType emotion){
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(getLikeKey(emotion), pageId.toString(), -1);
    }

    public void unlikePageTag(Long pageId, String tag){
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(getLikeKeyPageTag(tag), pageId.toString(), -1);    }

    public Set<String> getTopRankedPages(EmotionType emotion, int limit) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.reverseRange(getLikeKey(emotion), 0, limit - 1);
    }

    public Set<String> getTopRankedPagesByTag(String tag, int limit){
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.reverseRange(getLikeKeyPageTag(tag), 0, limit -1);
    }



    // Function to like a book
    public void likeBook(Long bookId, Category category){
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(getLikeKeyBook(category), bookId.toString(), 1);
    }

    // Function to unlike a book
    public void unlikeBook(Long bookId, Category category){
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(getLikeKeyBook(category), bookId.toString(), -1);
    }

    // Function to get top ranked books
    public Set<String> getTopRankedBooks(Category category, int limit) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.reverseRange(getLikeKeyBook(category), 0, limit - 1);
    }



    // Function to increment view count for a page
    public void incrementViewCountPage(Long pageId, EmotionType emotion){
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(getViewCountKey(emotion), pageId.toString(), 1);
    }

    public void incrementViewCountPageTag(Long pageId, String tag){
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(getViewCountKeyTag(tag), pageId.toString(), 1);
    }


    // Function to increment view count for a book
    public void incrementViewCountBook(Long bookId, Category category){
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(getViewCountKeyBook(category), bookId.toString(), 1);
    }

    // Function to get top viewed pages
    public Set<String> getTopViewedPages(EmotionType emotion, int limit) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.reverseRange(getViewCountKey(emotion), 0, limit - 1);
    }

    public Set<String> getTopViewedPagesByTag(String tag, int limit){
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.reverseRange(getViewCountKeyTag(tag), 0, limit - 1);
    }



    // Function to get top viewed books
    public Set<String> getTopViewedBooks(Category category, int limit) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.reverseRange(getViewCountKeyBook(category), 0, limit - 1);
    }

}
