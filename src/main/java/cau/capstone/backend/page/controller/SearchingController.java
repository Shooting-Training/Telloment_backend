package cau.capstone.backend.page.controller;


import cau.capstone.backend.User.dto.response.ResponseSearchUserDto;
import cau.capstone.backend.User.dto.response.ResponseUserLikeDto;
import cau.capstone.backend.User.service.UserService;
import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.page.dto.response.ResponseBookDto;
import cau.capstone.backend.page.dto.response.ResponsePageDto;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.service.BookService;
import cau.capstone.backend.page.service.PageService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "7. Searching")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/search")
public class SearchingController {

    private final PageService pageService;

    private final BookService bookService;

    private final UserService userService;


    @Operation(summary = "페이지 검색, 키워드로 타이틀, 콘텐트, 해시태그 검색")
    @GetMapping("/page")
    public ApiResponse<org.springframework.data.domain.Page<ResponsePageDto>> searchPages(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<ResponsePageDto> result = pageService.searchPagesWithKeywordAndPaging(keyword, pageable);
        return ApiResponse.success(result, "Search pages by keyword");
    }

    //해시태그로만 페이지 검색
    @Operation(summary = "해시태그로 페이지 검색")
    @GetMapping("/pages/hashtag")
    public ApiResponse<org.springframework.data.domain.Page<ResponsePageDto>> getPagesByHashtag(
            @RequestParam("hashtag") String hashtag,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<ResponsePageDto> result = pageService.getPagesByHashtag(hashtag, pageable);
        return ApiResponse.success(result, "Search pages by hashtag");
    }

//    @Operation(summary = "최근 24시간 이내에 생성된 페이지 반환")
//    @GetMapping("/page/recent")
//    public ResponseEntity<org.springframework.data.domain.Page<ResponsePageDto>> getPagesCreatedWithinLast24Hours(
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        org.springframework.data.domain.Page<ResponsePageDto> result = pageService.getPagesCreatedWithinLast24Hours(pageable);
//        return ResponseEntity.ok(result);
//    }

    @Operation(summary = "최근 24시간 내에 생성된 페이지를 감정, 해시태그 입력 값에 따라 조건부 반환")
    @GetMapping("/page/recent")
    public ApiResponse<org.springframework.data.domain.Page<ResponsePageDto>> getPagesCreatedWithinLast24HoursByEmotionAndHashtag(
            @RequestParam("emotion") String emotionType,
            @RequestParam("hashtag") String hashtag,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<ResponsePageDto> result = pageService.getPagesCreatedWithinLast24HoursByEmotionAndHashtag(emotionType, hashtag, pageable);
        return ApiResponse.success(result, "Search pages created within last 24 hours by emotion and hashtag");
    }

    @GetMapping("/books")
    public ApiResponse<org.springframework.data.domain.Page<ResponseBookDto>> searchBooks(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<ResponseBookDto> result = bookService.searchBooksWithKeywordAndPaging(keyword, pageable);
        return ApiResponse.success(result, "Search books by keyword");
    }


    @GetMapping("/users")
    public ApiResponse<org.springframework.data.domain.Page<ResponseSearchUserDto>> searchUsers(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<ResponseSearchUserDto> result = userService.searchUsersWithKeywordAndPaging(keyword, pageable);
        return ApiResponse.success(result, "Search users by keyword");
    }


    @GetMapping("/users/top10")
    public ApiResponse<List<ResponseUserLikeDto>> getTop10Users() {
        return ApiResponse.success(userService.findTop10UsersByLikes(), "Top 10 users by likes");
    }
}
