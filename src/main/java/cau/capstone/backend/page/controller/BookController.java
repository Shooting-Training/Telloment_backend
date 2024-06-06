package cau.capstone.backend.page.controller;


import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.page.dto.request.AddPageToBookDto;
import cau.capstone.backend.page.dto.request.CreateBookDto;
import cau.capstone.backend.page.dto.request.DeletePageFromBookDto;
import cau.capstone.backend.page.dto.response.CategoryDto;
import cau.capstone.backend.page.dto.response.ResponseBookDto;
import cau.capstone.backend.page.dto.response.ResponsePageDto;
import cau.capstone.backend.page.model.Book;
import cau.capstone.backend.page.model.Category;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "4. Book")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;


    @Operation(summary = "북 조회")
    @GetMapping("/{bookId}")
    public ApiResponse<ResponseBookDto> getBook(@PathVariable Long bookId){
        return ApiResponse.success(bookService.getBook(bookId), ResponseCode.BOOK_READ_SUCCESS.getMessage());
    }

    @Operation(summary = "북 생성")
    @PostMapping("/create")
    public ApiResponse<Long> createBook(@RequestBody @Valid CreateBookDto createBookDto, @RequestHeader String accessToken){
        return ApiResponse.success(bookService.createBook(createBookDto, accessToken), ResponseCode.BOOK_CREATE_SUCCESS.getMessage());
    }

    @Operation(summary = "유저의 이메일로 북 조회")
    @GetMapping("/{email}")
    public ApiResponse<List<ResponseBookDto>> getUserBook(@PathVariable String email){
        return ApiResponse.success(bookService.getBookListByEmail(email), ResponseCode.BOOK_READ_SUCCESS.getMessage());
    }


    @Operation(summary = "유저가 생성한 북 리스트 반환")
    @GetMapping("/list")
    public ApiResponse<List<ResponseBookDto>> getBookList(@RequestHeader String accessToken){

        return ApiResponse.success(bookService.getBookList(accessToken), ResponseCode.BOOK_READ_SUCCESS.getMessage());
    }

    @Operation(summary = "북 삭제")
    @DeleteMapping("/delete/{bookId}")
    public ApiResponse<Long> deleteBook(@PathVariable Long bookId, @RequestHeader String accessToken){

        return ApiResponse.success(bookService.deleteBook(bookId,accessToken), ResponseCode.BOOK_DELETE_SUCCESS.getMessage());
    }


    @Operation(summary = "페이지를 북에 추가")
    @PutMapping("/addPage")
    public ApiResponse<Long> addPageToBook(@RequestBody @Valid AddPageToBookDto addPageToBookDto){


        return ApiResponse.success(bookService.addPageToBook(addPageToBookDto), ResponseCode.BOOK_UPDATE_SUCCESS.getMessage());
    }


    @Operation(summary = "페이지를 북에서 삭제")
    @PutMapping("/deletePage")
    public ApiResponse<Long> deletePageFromBook(@RequestBody DeletePageFromBookDto deletePageFromBookDto, @RequestHeader String accessToken){


        return ApiResponse.success(bookService.deletePageFromBook(deletePageFromBookDto, accessToken), ResponseCode.BOOK_UPDATE_SUCCESS.getMessage());
    }

    @Operation(summary = "북의 페이지 리스트 반환" )
    @GetMapping("/pageList/{bookId}")
    public ApiResponse<List<ResponsePageDto>> getPageListFromBook(@PathVariable Long bookId){


        return ApiResponse.success(bookService.getPageListFromBook(bookId), ResponseCode.BOOK_READ_SUCCESS.getMessage());
    }


    @Operation(summary = "전체 북의 리스트를 페이저블하게 반환")
    @GetMapping("/allbooks")
    public ResponseEntity<org.springframework.data.domain.Page<ResponseBookDto>> getAllBooks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<ResponseBookDto> result = bookService.findAllBooks(pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "카테고리별 북의 리스트를 페이저블하게 반환")
    @GetMapping("/allbooks/category")
    public ResponseEntity<org.springframework.data.domain.Page<ResponseBookDto>> getAllBooks(
            @RequestParam(value = "category") String categoryCode,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<ResponseBookDto> result = bookService.findAllBooksByCategory(Category.getByCode(categoryCode.toUpperCase(Locale.ROOT)), pageable);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "북의 카테고리 리스트 반환")
    @GetMapping("/categories")
    public ApiResponse<List<CategoryDto>> getAllCategories() {
        return ApiResponse.success(bookService.getAllCategories(), ResponseCode.CATEGORY_READ_SUCCESS.getMessage());
    }


    @Operation(summary = "북을 좋아요")
    @PostMapping("/like/{bookId}")
    public ApiResponse<ResponseBookDto> likeBook(@PathVariable Long bookId, @RequestHeader String accessToken){
        return ApiResponse.success(bookService.likeBook(bookId, accessToken), ResponseCode.BOOK_LIKE_SUCCESS.getMessage());
    }

    @Operation(summary = "북 좋아요 취소")
    @DeleteMapping("/like/{bookId}")
    public ApiResponse<ResponseBookDto> unlikeBook(@PathVariable Long bookId, @RequestHeader String accessToken){
        return ApiResponse.success(bookService.unlikeBook(bookId, accessToken), ResponseCode.BOOK_DISLIKE_SUCCESS.getMessage());
    }


}
