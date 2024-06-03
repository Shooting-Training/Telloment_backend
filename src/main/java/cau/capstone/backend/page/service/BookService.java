package cau.capstone.backend.page.service;

import cau.capstone.backend.User.model.User;
import cau.capstone.backend.User.model.repository.UserRepository;
import cau.capstone.backend.global.redis.RankingService;
import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.global.util.api.ResponseCode;
import cau.capstone.backend.global.util.exception.BookException;
import cau.capstone.backend.global.util.exception.PageException;
import cau.capstone.backend.global.util.exception.UserException;
import cau.capstone.backend.page.dto.request.AddPageToBookDto;
import cau.capstone.backend.page.dto.request.CreateBookDto;
import cau.capstone.backend.page.dto.request.DeletePageFromBookDto;
import cau.capstone.backend.page.dto.response.CategoryDto;
import cau.capstone.backend.page.dto.response.ResponseBookDto;
import cau.capstone.backend.page.dto.response.ResponsePageDto;
import cau.capstone.backend.page.model.*;
import cau.capstone.backend.page.model.repository.BookRepository;
import cau.capstone.backend.page.model.repository.HashtagRepository;
import cau.capstone.backend.page.model.repository.LikeRepository;
import cau.capstone.backend.page.model.repository.PageRepository;
import com.amazonaws.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PageRepository pageRepository;
    private final HashtagRepository hashtagRepository;
    private final LikeRepository likeRepository;

    private final RankingService rankingService;
    private final LikeService likeService;
    private final PageService pageService;

    private final JwtTokenProvider jwtTokenProvider;



    @Transactional
    public long createBook(CreateBookDto createBookDto, String accessToken) {

        String email = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));

        if(Category.getByCode(createBookDto.getCategoryCode()) == null){
            throw new BookException(ResponseCode.CATEGORY_NOT_FOUND);
        }

        Book book = Book.createBook(user, createBookDto.getBookName(), createBookDto.getCategoryCode());

        setHashtagsToBook(book, createBookDto.getHashtags());

        return bookRepository.save(book).getId();
    }


    @Transactional
    public Book createBookWithHashtags(Book book, Set<String> hashtags) {
        Set<Hashtag> hashtagEntities = new HashSet<>();

        for (String tag : hashtags) {
            Hashtag hashtag = hashtagRepository.findByTag(tag)
                    .orElseGet(() -> hashtagRepository.save(new Hashtag(tag)));
            hashtagEntities.add(hashtag);
        }

        book.setHashtags(hashtagEntities);
        return bookRepository.save(book);

    }

    @Transactional
    public void setHashtagsToBook(Book book, Set<String> hashtags) {
        Set<Hashtag> existingHashtags = book.getHashtags();

        for (String tag : hashtags) {
            Hashtag hashtag = hashtagRepository.findByTag(tag)
                    .orElseGet(() -> hashtagRepository.save(new Hashtag(tag)));
            existingHashtags.add(hashtag);
        }

        book.setHashtags(existingHashtags);

    }


    @Transactional
    public List<ResponseBookDto> getBookList(String accessToken){

        String email = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        Long userId = user.getId();

        List<Book> bookList = bookRepository.findAllByUserId(userId);

        List<ResponseBookDto> responseBookDtoList = new ArrayList<>();

        for (Book book : bookList) {
            ResponseBookDto responseBookDto = ResponseBookDto.from(book);
            responseBookDto.setTotalLikeCount(likeService.countTotalLikesForBook(book.getId()));

            System.out.println(responseBookDto.getBookName() + " " + responseBookDto.getTotalLikeCount());
            responseBookDtoList.add(responseBookDto);
        }


        return responseBookDtoList;
    }


    @Transactional
    public ResponseBookDto likeBook(Long bookId, String accessToken){
        String userEmail = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        Book book = getBookById(bookId);

        rankingService.likeBook(bookId, book.getCategory());

        if(likeRepository.existsByLikeTypeAndTargetIdAndUserId(LikeType.BOOK, bookId, user.getId())){
            throw new BookException(ResponseCode.LIKE_ALREADY_EXIST);
        }

        Like like = Like.createLike(user, LikeType.BOOK, bookId);
        user.addLike(like);

        likeRepository.save(like);
        userRepository.save(user);

        ResponseBookDto responseBookDto = ResponseBookDto.from(book);
        responseBookDto.setTotalLikeCount(likeService.countTotalLikesForBook(bookId));

        return responseBookDto;
    }

    @Transactional
    public ResponseBookDto unlikeBook(Long bookId, String accessToken) {
        String userEmail = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        Book book = getBookById(bookId);

        rankingService.unlikeBook(bookId, book.getCategory());

        Like like = likeRepository.findByLikeTypeAndTargetIdAndUserId(LikeType.BOOK, bookId, user.getId())
                .orElseThrow(() -> new BookException(ResponseCode.LIKE_NOT_FOUND));

        user.removeLike(like);

        likeRepository.delete(like);
        userRepository.save(user);

        return ResponseBookDto.from(book);
    }


    @Transactional
    public long deleteBook(Long bookId, String accessToken){
        String email = jwtTokenProvider.getUserEmail(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND));
        Long userId = user.getId();

        validateBook(bookId, userId);

        Book book = getBookById(bookId);
        likeService.deleteBookLike(book);

        user.getBooks().remove(book);

        for (Page page : book.getPages()) {
            pageService.deletePage(page.getId(), userId);
        }

        bookRepository.delete(book);
//유저의 좋아요 리스트도 제거?


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

        validatePageInBook(pageId, bookId, jwtTokenProvider.getUserEmail(accessToken));

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



    public org.springframework.data.domain.Page<ResponseBookDto> searchBooksWithKeywordAndPaging(String keyword, Pageable pageable) {
        Pageable sortedByCreationDateDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "updatedAt")
        );

        org.springframework.data.domain.Page<Book> books = bookRepository.findByKeywordWithPaging(keyword, sortedByCreationDateDesc);

        List<ResponseBookDto> responseBookDtoList = new ArrayList<>();

        for (Book book : books) {
            ResponseBookDto responseBookDto = ResponseBookDto.from(book);
            responseBookDto.setTotalLikeCount(likeService.countTotalLikesForBook(book.getId()));
            responseBookDtoList.add(responseBookDto);
        }

        return new PageImpl<>(responseBookDtoList, pageable, books.getTotalElements());
    }

    public org.springframework.data.domain.Page<ResponseBookDto> findAllBooks(Pageable pageable) {
        Pageable sortedByCreationDateDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "updatedAt")
        );

        org.springframework.data.domain.Page<Book> books = bookRepository.findAll(sortedByCreationDateDesc);

        List<ResponseBookDto> responseBookDtoList = new ArrayList<>();

        for (Book book : books) {
            ResponseBookDto responseBookDto = ResponseBookDto.from(book);
            responseBookDto.setTotalLikeCount(likeService.countTotalLikesForBook(book.getId()));
            responseBookDtoList.add(responseBookDto);
        }

        return new PageImpl<>(responseBookDtoList, pageable, books.getTotalElements());
    }

    public org.springframework.data.domain.Page<ResponseBookDto> findAllBooksByCategory(Category category,Pageable pageable) {
        Pageable sortedByCreationDateDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "updatedAt")
        );

        org.springframework.data.domain.Page<Book> books = bookRepository.findByCategory(category, sortedByCreationDateDesc);

        List<ResponseBookDto> responseBookDtoList = new ArrayList<>();

        for (Book book : books) {
            ResponseBookDto responseBookDto = ResponseBookDto.from(book);
            responseBookDto.setTotalLikeCount(likeService.countTotalLikesForBook(book.getId()));
            responseBookDtoList.add(responseBookDto);
        }

        return new PageImpl<>(responseBookDtoList, pageable, books.getTotalElements());
    }


    public List<CategoryDto> getAllCategories() {

        List<CategoryDto> dtoList = new ArrayList<>();

        for (Category category : Category.values()){

            int bookCount = bookRepository.countAllByCategory(category);
            System.out.println(category + " " + bookCount);
            CategoryDto categoryDto = new CategoryDto(category.getCode(), category.getName(), bookCount);

            dtoList.add(categoryDto);
        }


        return dtoList;
    }

    public Set<ResponseBookDto> parseTopRankedBooks(Set<String> bookIds) {
        Set<ResponseBookDto> topRankedBooks = new LinkedHashSet<>();
        for (String bookId : bookIds) {
            Book book = bookRepository.findById(Long.parseLong(bookId))
                    .orElseThrow(() -> new BookException(ResponseCode.BOOK_NOT_FOUND));
            ResponseBookDto responseBookDto = ResponseBookDto.from(book);
            responseBookDto.setTotalLikeCount(likeService.countTotalLikesForBook(book.getId()));
            topRankedBooks.add(responseBookDto);
        }
        return topRankedBooks;
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

    private void validatePageInBook(Long pageId, Long bookId, String userEmail){
        Long userId = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ResponseCode.USER_NOT_FOUND)).getId();

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
