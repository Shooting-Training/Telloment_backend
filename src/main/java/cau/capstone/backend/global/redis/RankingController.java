package cau.capstone.backend.global.redis;

import cau.capstone.backend.User.model.Category;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Api(tags = "6. Ranking")
@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping("/top-like")
    public Set<String> getTopPosts(@RequestParam String categoryCode, @RequestParam int topN) {
        return rankingService.getTopPagesLike(categoryCode, topN);
    }

    @GetMapping("/top-viewed")
    public Set<String> getTopViewedPosts(@RequestParam String categoryCode, @RequestParam int topN) {
        return rankingService.getTopViewedPages(categoryCode, topN);
    }

//    @GetMapping("/{pageId}/rank")
//    public Long getPostRank(@PathVariable Long pageId, @RequestParam String categoryCode) {
//        return rankingService.getPageRank(pageId, categoryCode);
//    }
//
//    @GetMapping("/{postId}/score")
//    public Double getPostScore(@PathVariable Long postId, @RequestParam String categoryCode) {
//        return rankingService.getPageScore(postId, categoryCode);
//    }
}