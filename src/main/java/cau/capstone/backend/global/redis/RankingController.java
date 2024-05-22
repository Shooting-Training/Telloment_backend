//package cau.capstone.backend.global.redis;
//
//import cau.capstone.backend.User.model.Category;
//import io.swagger.annotations.Api;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Set;
//
//@Api(tags = "6. Ranking")
//@RestController
//@RequestMapping("/api/ranking")
//public class RankingController {
//
//    @Autowired
//    private RankingService rankingService;
//
//    @GetMapping("/top")
//    public Set<String> getTopPosts(@RequestParam String categoryCode, @RequestParam int topN) {
//        return rankingService.getTopPages(categoryCode, topN);
//    }
//
//    @GetMapping("/{postId}/rank")
//    public Long getPostRank(@PathVariable Long postId, @RequestParam String categoryCode) {
//        return rankingService.getPageRank(postId, categoryCode);
//    }
//
//    @GetMapping("/{postId}/score")
//    public Double getPostScore(@PathVariable Long postId, @RequestParam String categoryCode) {
//        return rankingService.getPageScore(postId, categoryCode);
//    }
//}