//package cau.capstone.backend.service;
//
//
//import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
//import cau.capstone.backend.page.dto.request.AddPageToBookDto;
//import cau.capstone.backend.page.dto.request.CreateBookDto;
//import cau.capstone.backend.page.dto.request.DeletePageFromBookDto;
//import cau.capstone.backend.page.dto.response.ResponseBookDto;
//import cau.capstone.backend.page.dto.response.ResponsePageDto;
//import cau.capstone.backend.page.model.repository.BookRepository;
//import cau.capstone.backend.page.service.BookService;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//@ExtendWith(MockitoExtension.class)
//public class BookServiceTest {
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @Mock
//    private JwtTokenProvider jwtTokenProvider;
//
//    @InjectMocks
//    private BookService bookService;
//
//    @Test
//    public void createBookTest() {
//        CreateBookDto createBookDto = new CreateBookDto("Sample Book");
//        Long userId = 1L;
//        String accessToken = "access-token";
//
//        when(jwtTokenProvider.getUserPk(accessToken)).thenReturn(userId);
//        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);
//
//        Long bookId = bookService.createBook(createBookDto, accessToken);
//
//        assertNotNull(bookId);
//        verify(bookRepository, times(1)).save(any(Book.class));
//    }
//
//    @Test
//    public void getBookListTest() {
//        String accessToken = "access-token";
//        Long userId = 1L;
//
//        when(jwtTokenProvider.getUserPk(accessToken)).thenReturn(userId);
//        when(bookRepository.findAllByUserId(userId)).thenReturn(Arrays.asList(new Book(), new Book()));
//
//        List<ResponseBookDto> result = bookService.getBookList(accessToken);
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        verify(bookRepository, times(1)).findAllByUserId(userId);
//    }
//
//
//    @Test
//    public void deleteBookTest() {
//        String accessToken = "access-token";
//        Long userId = 1L;
//        Long bookId = 2L;
//
//        when(jwtTokenProvider.getUserPk(accessToken)).thenReturn(userId);
//        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));
//
//        Long result = bookService.deleteBook(bookId, accessToken);
//
//        assertEquals(bookId, result);
//        verify(bookRepository, times(1)).delete(any(Book.class));
//    }
//
//    @Test
//    public void testAddPageToBook() {
//        // 준비
//        AddPageToBookDto addPageToBookDto = new AddPageToBookDto(1L, 2L);
//        Page page = new Page();
//        page.setId(1L);
//        Book book = new Book();
//        book.setId(2L);
//        when(pageRepository.findById(addPageToBookDto.getPageId())).thenReturn(Optional.of(page));
//        when(bookRepository.findById(addPageToBookDto.getBookId())).thenReturn(Optional.of(book));
//        when(bookRepository.save(any(Book.class))).thenReturn(book);
//
//        // 실행
//        long bookId = bookService.addPageToBook(addPageToBookDto);
//
//        // 검증
//        assertEquals(2L, bookId);
//        assertTrue(book.getPages().contains(page));
//        verify(pageRepository, times(1)).save(page);
//        verify(bookRepository, times(1)).save(book);
//    }
//
//    @Test
//    public void testDeletePageFromBook() {
//        // 준비
//        Long pageId = 1L;
//        Long bookId = 2L;
//        String accessToken = "access-token";
//        Long userId = 3L;
//        DeletePageFromBookDto deletePageFromBookDto = new DeletePageFromBookDto(pageId, bookId);
//        Page page = new Page();
//        page.setId(pageId);
//        Book book = new Book();
//        book.setId(bookId);
//        book.addPage(page);
//        when(jwtTokenProvider.getUserPk(accessToken)).thenReturn(userId);
//        when(pageRepository.findById(pageId)).thenReturn(Optional.of(page));
//        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
//
//        // 실행
//        long resultBookId = bookService.deletePageFromBook(deletePageFromBookDto, accessToken);
//
//        // 검증
//        assertEquals(bookId, resultBookId);
//        assertFalse(book.getPages().contains(page));
//        assertNull(page.getBook());
//        verify(bookRepository, times(1)).save(book);
//        verify(pageRepository, times(1)).save(page);
//    }
//
//    @Test
//    public void testGetPageListFromBook() {
//        // 준비
//        Long bookId = 1L;
//        Book book = new Book();
//        book.setId(bookId);
//        Page page1 = new Page();
//        Page page2 = new Page();
//        book.addPage(page1);
//        book.addPage(page2);
//        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
//
//        // 실행
//        List<ResponsePageDto> result = bookService.getPageListFromBook(bookId);
//
//        // 검증
//        assertEquals(2, result.size());
//        verify(bookRepository, times(1)).findById(bookId);
//    }
//
//}
