package cau.capstone.backend.page.controller;


import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.global.util.api.ApiResponse;
import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.page.dto.request.AddPageToBookDto;
import cau.capstone.backend.page.dto.request.CreateBookDto;
import cau.capstone.backend.page.dto.response.ResponseBookDto;
import cau.capstone.backend.page.dto.response.ResponsePageDto;
import cau.capstone.backend.page.model.Book;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "4. Book")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/book")
public class BookController {

    private final BookService bookService;
    private final JwtTokenProvider jwtTokenProvider;


    @Operation(summary = "북 생성")
    @PostMapping("/create")
    public void createBook(@RequestBody @Valid CreateBookDto createBookDto, @RequestHeader String accessToken){
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        bookService.createBook(createBookDto, userId);
    }

    @Operation(summary = "북 리스트 반환")
    @GetMapping("/list")
    public ApiResponse<List<ResponseBookDto>> getBookList(@RequestHeader String accessToken){
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        List<Book> bookList = bookService.BookList(userId);

        List<ResponseBookDto> responseBookDtoList = bookList.stream()
                .map(ResponseBookDto::from)
                .collect(Collectors.toList());

        return ApiResponse.success(responseBookDtoList, ResponseCode.BOOK_READ_SUCCESS.getMessage());
    }

    @Operation(summary = "북 삭제")
    @DeleteMapping("/delete/{bookId}")
    public void deleteBook(@PathVariable Long bookId, @RequestHeader String accessToken){
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        bookService.deleteBook(bookId,userId);
    }


    @Operation(summary = "페이지를 북에 추가")
    @PutMapping("/addPage")
    public void addPageToBook(@RequestBody @Valid AddPageToBookDto addPageToBookDto){
        bookService.addPageToBook(addPageToBookDto);
    }


    @Operation(summary = "페이지를 북에서 삭제")
    @DeleteMapping("/deletePage/{bookId}/{pageId}")
    public void deletePageFromBook(@PathVariable Long bookId, @PathVariable Long pageId, @RequestHeader String accessToken){
        Long userId = jwtTokenProvider.getUserPk(accessToken);

        bookService.deletePageFromBook(bookId, pageId, userId);
    }

    @Operation(summary = "북의 페이지 리스트 반환" )
    @GetMapping("/pageList/{bookId}")
    public ApiResponse<List<ResponsePageDto>> getPageListFromBook(@PathVariable Long bookId){
        List<Page> pageList = bookService.getPageListFromBook(bookId);

        List<ResponsePageDto> responsePageDtoList = pageList.stream()
                .map(ResponsePageDto::from)
                .collect(Collectors.toList());

        return ApiResponse.success(responsePageDtoList, ResponseCode.BOOK_READ_SUCCESS.getMessage());
    }



}
