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
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "4. Book")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;


    @Operation(summary = "북 생성")
    @PostMapping("/create")
    public ApiResponse<Long> createBook(@RequestBody @Valid CreateBookDto createBookDto, @RequestHeader String accessToken){
        return ApiResponse.success(bookService.createBook(createBookDto, accessToken), ResponseCode.BOOK_CREATE_SUCCESS.getMessage());
    }


    @Operation(summary = "북 리스트 반환")
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


    @GetMapping("/search")
    public ResponseEntity<org.springframework.data.domain.Page<Book>> searchBooks(
            @RequestParam("name") String name,
            @RequestParam("hashtags") Set<String> hashtags,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<Book> result = bookService.searchBooksByNameOrHashtags(name, hashtags, pageable);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/categories")
    public ApiResponse<List<CategoryDto>> getAllCategories() {
        return ApiResponse.success(bookService.getAllCategories(), ResponseCode.CATEGORY_READ_SUCCESS.getMessage());
    }


}
