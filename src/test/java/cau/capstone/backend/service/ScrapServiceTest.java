package cau.capstone.backend.service;

import cau.capstone.backend.User.model.User;
import cau.capstone.backend.User.model.repository.UserRepository;
import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.global.util.exception.ScrapException;
import cau.capstone.backend.page.dto.request.CreatePageFromScrapDto;
import cau.capstone.backend.page.dto.request.CreateScrapDto;
import cau.capstone.backend.page.dto.response.ResponseScrapDto;
import cau.capstone.backend.page.model.Book;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.model.Scrap;
import cau.capstone.backend.page.model.repository.PageRepository;
import cau.capstone.backend.page.model.repository.ScrapRepository;
import cau.capstone.backend.page.service.ScrapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScrapServiceTest {

    @InjectMocks
    private ScrapService scrapService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private ScrapRepository scrapRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PageRepository pageRepository;


    @BeforeEach
    public void setup() {



        Scrap scrap = Scrap.createScrap(user, page);

        book.addPage(page);
        user.addBook(book);

    }


//    @DisplayName("스크랩 저장 - 이미 스크랩한 페이지인 경우")
//    @Test
//    public void testSaveScrap_AlreadyScrapped() {
//        // given
//        String accessToken = "mock-access-token";
//        Long userId = 1L;
//        Long pageId = 1L;
//        CreateScrapDto createScrapDto = new CreateScrapDto(pageId);
//
//        when(jwtTokenProvider.getUserPk(accessToken)).thenReturn(userId);
//        when(scrapRepository.existsByUserIdAndPageId(userId, pageId)).thenReturn(true);
//
//        // when
//        scrapService.saveScrap(createScrapDto, accessToken);
//
//        // then
//        // ScrapException is expected
//    }

    @DisplayName("스크랩 저장 - 성공")
    @Test
    public void testSaveScrap_Success() {


        //given
        Long scrapId = 1L;
        Long pageId = 1L;
        Long userId = 1L;

        CreateScrapDto createScrapDto = new CreateScrapDto(pageId);




        //when


        //then


    }

    @Test
    public void testGetScrapList() {
        // given
        String accessToken = "mock-access-token";
        Long userId = 1L;
        List<Scrap> mockScrapList = Arrays.asList(new Scrap(), new Scrap()); // 적절한 생성자 또는 빌더를 사용하여 초기화

        when(jwtTokenProvider.getUserPk(accessToken)).thenReturn(userId);
        when(scrapRepository.findAllByUserId(userId)).thenReturn(mockScrapList);

        // when
        List<ResponseScrapDto> result = scrapService.getScrapList(accessToken);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
    }


    @Test
    public void testDeleteScrap() {
        Long userId = 1L;
        Long scrapId = 2L;
        String accessToken = "access-token";

        Scrap mockScrap = new Scrap();
        Page mockPage = new Page();
        mockScrap.setPage(mockPage);

        when(jwtTokenProvider.getUserPk(accessToken)).thenReturn(userId);
        when(scrapRepository.findById(scrapId)).thenReturn(Optional.of(mockScrap));

        Long returnedScrapId = scrapService.deleteScrap(scrapId, accessToken);

        verify(scrapRepository, times(1)).deleteById(scrapId);
        verify(pageRepository, times(1)).save(mockPage);
        assertEquals(scrapId, returnedScrapId);
    }

    @Test
    public void testCreatePageFromScrap() {
        Long userId = 1L;
        Long scrapId = 2L;
        Long bookId = 3L;
        String accessToken = "access-token";

        CreatePageFromScrapDto createPageFromScrapDto = new CreatePageFromScrapDto();
        createPageFromScrapDto.setScrapId(scrapId);
        createPageFromScrapDto.setBookId(bookId);

        Scrap mockScrap = new Scrap();
        Book mockBook = new Book();
        User mockUser = new User();
        Page mockPage = new Page();

        when(jwtTokenProvider.getUserPk(accessToken)).thenReturn(userId);
        when(scrapRepository.findById(scrapId)).thenReturn(Optional.of(mockScrap));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(pageRepository.save(any(Page.class))).thenReturn(mockPage);

        Long returnedPageId = scrapService.createPageFromScrap(createPageFromScrapDto, accessToken);

        assertNotNull(returnedPageId);
        verify(pageRepository, times(1)).save(any(Page.class));
    }


}
