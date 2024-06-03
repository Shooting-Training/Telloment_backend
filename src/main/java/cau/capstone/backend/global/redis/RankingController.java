package cau.capstone.backend.global.redis;

import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.page.dto.response.ResponseBookDto;
import cau.capstone.backend.page.dto.response.ResponsePageDto;
import cau.capstone.backend.page.model.Category;
import cau.capstone.backend.page.model.EmotionType;
import cau.capstone.backend.page.service.BookService;
import cau.capstone.backend.page.service.PageService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Set;

@Api(tags = "6. Ranking")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingService rankingService;

    private final PageService pageService;
    private final BookService bookService;


    @Operation(summary = "좋아요 수 상위 N개 페이지 반환, 감정 기준으로 분류")
    @GetMapping("/page/emotion/topliked")
    public ApiResponse<Set<ResponsePageDto>> getTopLikedPages(@RequestParam("emotion") String emotionCode, @RequestParam("top") int topN) {
        Set<ResponsePageDto> responsePageDtos = pageService.parseTopRankedPages(rankingService.getTopRankedPages(EmotionType.getByCode(emotionCode.toUpperCase(Locale.ROOT)), topN));
        return ApiResponse.success(responsePageDtos, "Top " + topN + " liked pages" + " with emotion " + emotionCode);
    }

    @Operation(summary = "좋아요 수 상위 N개 책 반환, 카테고리 기준으로 분류")
    @GetMapping("/book/category/topliked")
    public ApiResponse<Set<ResponseBookDto>> getTopLikeBooks(@RequestParam("category") String categoryCode, @RequestParam("top") int topN) {
        Set<ResponseBookDto> responseBookDtos = bookService.parseTopRankedBooks(rankingService.getTopRankedBooks(Category.getByCode(categoryCode.toUpperCase(Locale.ROOT)), topN));
        return ApiResponse.success(responseBookDtos, "Top " + topN + " liked books" + " with category " + categoryCode);
    }


    @Operation(summary = "조회수 상위 N개 페이지 반환, 감정 기준으로 분류")
    @GetMapping("/page/emotion/topviewed")
    public ApiResponse<Set<ResponsePageDto>> getTopViewedPages(@RequestParam("emotion") String emotionCode, @RequestParam("top") int topN) {
        Set<ResponsePageDto> responsePageDtos = pageService.parseTopRankedPages(rankingService.getTopViewedPages(EmotionType.getByCode(emotionCode.toUpperCase(Locale.ROOT)), topN));
        return ApiResponse.success(responsePageDtos, "Top " + topN + " viewed pages" + " with emotion " + emotionCode);
    }

    @Operation(summary = "조회수 상위 N개 책 반환, 카테고리 기준으로 분류")
    @GetMapping("/book/category/topviewed")
    public ApiResponse<Set<ResponseBookDto>> getTopViewedBooks(@RequestParam("category") String categoryCode, @RequestParam("top") int topN) {
        Set<ResponseBookDto> responseBookDtos = bookService.parseTopRankedBooks(rankingService.getTopViewedBooks(Category.getByCode(categoryCode.toUpperCase(Locale.ROOT)), topN));
        return ApiResponse.success(responseBookDtos, "Top " + topN + " viewed books" + " with category " + categoryCode);
    }

    @Operation(summary = "태그에 따라서 페이지를 추천")
    @GetMapping("/page/tag/topliked")
    public ApiResponse<Set<ResponsePageDto>> getTopLikedPagesTag(@RequestParam("tag") String tag, @RequestParam("top") int topN){
        Set<ResponsePageDto> responsePageDtos = pageService.parseTopRankedPages(rankingService.getTopRankedPagesByTag(tag, topN));
        return ApiResponse.success(responsePageDtos, "Top " + topN + " liked pages" + " with tag " + tag);
    }

    @Operation(summary = "태그에 따라서 페이지를 추천(뷰 기반)")
    @GetMapping("/page/tag/topviewed")
    public ApiResponse<Set<ResponsePageDto>> getTopViewedPagesTag(@RequestParam("tag") String tag, @RequestParam("top") int topN){
        Set<ResponsePageDto> responsePageDtos = pageService.parseTopRankedPages(rankingService.getTopViewedPagesByTag(tag, topN));
        return ApiResponse.success(responsePageDtos, "Top " + topN + " viewed pages" + " with tag " + tag);

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