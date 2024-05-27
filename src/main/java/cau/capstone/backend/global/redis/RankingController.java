package cau.capstone.backend.global.redis;

import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.page.dto.response.ResponseBookDto;
import cau.capstone.backend.page.dto.response.ResponsePageDto;
import cau.capstone.backend.page.model.Category;
import cau.capstone.backend.page.model.EmotionType;
import cau.capstone.backend.page.service.BookService;
import cau.capstone.backend.page.service.PageService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Api(tags = "6. Ranking")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService rankingService;

    private final PageService pageService;
    private final BookService bookService;

    @GetMapping("/page/emotion/topliked")
    public ApiResponse<Set<ResponsePageDto>> getTopLikedPages(@RequestParam String emotionCode, @RequestParam int topN) {
        Set<ResponsePageDto> responsePageDtos = pageService.parseTopRankedPaged(rankingService.getTopRankedPages(EmotionType.getByCode(emotionCode), topN));
        return ApiResponse.success(responsePageDtos, "Top " + topN + " liked pages" + " with emotion " + emotionCode);
    }

    @GetMapping("/book/category/topliked")
    public ApiResponse<Set<ResponseBookDto>> getTopLikeBooks(@RequestParam String categoryCode, @RequestParam int topN) {
        Set<ResponseBookDto> responseBookDtos = bookService.parseTopRankedBooks(rankingService.getTopRankedBooks(Category.getByCode(categoryCode), topN));
        return ApiResponse.success(responseBookDtos, "Top " + topN + " liked books" + " with category " + categoryCode);
    }

    @GetMapping("page/emotion/topviewed")
    public ApiResponse<Set<ResponsePageDto>> getTopViewedPages(@RequestParam String emotionCode, @RequestParam int topN) {
        Set<ResponsePageDto> responsePageDtos = pageService.parseTopRankedPaged(rankingService.getTopViewedPages(EmotionType.getByCode(emotionCode), topN));
        return ApiResponse.success(responsePageDtos, "Top " + topN + " viewed pages" + " with emotion " + emotionCode);
    }

    @GetMapping("book/category/topviewed")
    public ApiResponse<Set<ResponseBookDto>> getTopViewedBooks(@RequestParam String categoryCode, @RequestParam int topN) {
        Set<ResponseBookDto> responseBookDtos = bookService.parseTopRankedBooks(rankingService.getTopViewedBooks(Category.getByCode(categoryCode), topN));
        return ApiResponse.success(responseBookDtos, "Top " + topN + " viewed books" + " with category " + categoryCode);
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