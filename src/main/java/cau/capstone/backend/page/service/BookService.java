package cau.capstone.backend.page.service;

import cau.capstone.backend.User.model.User;
import cau.capstone.backend.User.model.repository.UserRepository;
import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.global.util.exception.BookException;
import cau.capstone.backend.global.util.exception.PageException;
import cau.capstone.backend.global.util.exception.UserException;
import cau.capstone.backend.page.dto.request.AddPageToBookDto;
import cau.capstone.backend.page.dto.request.CreateBookDto;
import cau.capstone.backend.page.dto.request.DeletePageFromBookDto;
import cau.capstone.backend.page.dto.response.ResponseBookDto;
import cau.capstone.backend.page.dto.response.ResponsePageDto;
import cau.capstone.backend.page.model.Book;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.model.repository.BookRepository;
import cau.capstone.backend.page.model.repository.PageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PageRepository pageRepository;

    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public long createBook(CreateBookDto createBookDto, String accessToken) {
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        User user = getUserById(userId);
        Book book = Book.createBook(user, createBookDto.getBookName());

        return bookRepository.save(book).getId();
    }


    @Transactional
    public List<ResponseBookDto> getBookList(String accessToken){
        Long userId = jwtTokenProvider.getUserPk(accessToken);

        List<Book> bookList = bookRepository.findAllByUserId(userId);

        List<ResponseBookDto> responseBookDtoList = bookList.stream()
                .map(ResponseBookDto::from)
                .collect(Collectors.toList());


        return responseBookDtoList;
    }



    @Transactional
    public long deleteBook(Long bookId, String accessToken){
        Long userId = jwtTokenProvider.getUserPk(accessToken);
        validateBook(bookId, userId);

        Book book = getBookById(bookId);
        bookRepository.delete(book);

        return bookId;
    }



    //페이지를 북에 추가
    @Transactional
    public long addPageToBook(AddPageToBookDto addPageToBookDto) {
        Page page = getPageById(addPageToBookDto.getPageId());
        Book book = getBookById(addPageToBookDto.getBookId());

        page.setBook(book);
        book.addPage(page);

        pageRepository.save(page);
        return bookRepository.save(book).getId();
    }


    //페이지를 북에서 삭제
    @Transactional
    public long deletePageFromBook(DeletePageFromBookDto deletePageFromBookDto, String accessToken){
        Long pageId = deletePageFromBookDto.getPageId();
        Long bookId = deletePageFromBookDto.getBookId();

        validatePageInBook(pageId, bookId, jwtTokenProvider.getUserPk(accessToken));

        Page page = getPageById(pageId);
        Book book = getBookById(bookId);

        page.setBook(null);
        book.removePage(page);

        bookRepository.save(book);
        pageRepository.save(page);

        return bookId;
    }


    @Transactional
    public List<ResponsePageDto> getPageListFromBook(Long bookId){
        Book book = getBookById(bookId);

        List<ResponsePageDto> responsePageDtoList = book.getPages().stream()
                .map(ResponsePageDto::from)
                .collect(Collectors.toList());

        return responsePageDtoList;
    }


    private User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
    }

    private Page getPageById(Long pageId){
        return pageRepository.findById(pageId)
                .orElseThrow(() -> new PageException(ResponseCode.PAGE_NOT_FOUND));
    }

    private Book getBookById(Long bookId){
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new PageException(ResponseCode.BOOK_NOT_FOUND));
    }

    public void validateBook(Long bookId, Long userId){
        User user = getUserById(userId);
        Book book = getBookById(bookId);

        if(!user.getBooks().contains(book)){
            throw new BookException(ResponseCode.BOOK_NOT_FOUND);
        }
    }

    private void validatePage(Long pageId, Long userId){
        if(!pageRepository.existsById(pageId)){
            throw new PageException(ResponseCode.PAGE_NOT_FOUND);
        }
        if (!pageRepository.existsByIdAndUserId(pageId, userId)){ //페이지의 주인이 아닌지 확인
            throw new PageException(ResponseCode.PAGE_NOT_OWNED);
        }
    }

    private void validatePageInBook(Long pageId, Long bookId, Long userId){
        validatePage(pageId, userId);
        validateBook(bookId, userId);

        if(!pageRepository.existsById(pageId)){
            throw new PageException(ResponseCode.PAGE_NOT_FOUND);
        }
        if (!pageRepository.existsByIdAndBookId(pageId, bookId)){ //북에 속한 페이지인지 확인
            throw new PageException(ResponseCode.PAGE_NOT_IN_BOOK);
        }
    }




}
