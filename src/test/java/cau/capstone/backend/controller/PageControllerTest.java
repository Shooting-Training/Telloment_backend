//package cau.capstone.backend.controller;
//
//
//import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
//import cau.capstone.backend.page.controller.PageController;
//import cau.capstone.backend.page.dto.request.CreatePageDto;
//import cau.capstone.backend.page.dto.request.LikePageDto;
//import cau.capstone.backend.page.dto.request.LinkPageDto;
//import cau.capstone.backend.page.dto.request.UpdatePageDto;
//import cau.capstone.backend.page.dto.response.ResponsePageDto;
//import cau.capstone.backend.page.service.PageService;
//
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@WebMvcTest(controllers = PageController.class)
//public class PageControllerTest {
//
//    @InjectMocks
//    private PageController pageController;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private PageService pageService;
//
//    @MockBean
//    private JwtTokenProvider jwtTokenProvider;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    public void setup() {
//        objectMapper = new ObjectMapper();
//        mockMvc = standaloneSetup(pageController).build();
//    }
//
//    @Test
//    void getPageTest() throws Exception {
//        Long pageId = 1L;
//        ResponsePageDto responsePageDto = new ResponsePageDto(); // 필요한 필드를 채워주세요.
//        given(pageService.getPage(pageId)).willReturn(responsePageDto);
//
//        mockMvc.perform(get("/" + pageId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").exists());
//    }
//
//    @Test
//    void savePageTest() throws Exception {
//        CreatePageDto createPageDto = new CreatePageDto(); // 필요한 필드를 채워주세요.
//        String accessToken = "Bearer yourAccessToken";
//        given(pageService.createPage(createPageDto, accessToken)).willReturn(1L);
//
//        mockMvc.perform(post("/save")
//                        .header("Authorization", accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createPageDto)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void getPageListTest() throws Exception {
//        String accessToken = "Bearer yourAccessToken";
//        Long userId = 1L;
//        given(jwtTokenProvider.getUserPk(accessToken)).willReturn(userId);
//        given(pageService.getPageList(userId)).willReturn(List.of(new ResponsePageDto()));
//
//        mockMvc.perform(get("/list")
//                        .header("Authorization", accessToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").isArray());
//    }
//
//    @Test
//    void updatePageTest() throws Exception {
//        UpdatePageDto updatePageDto = new UpdatePageDto(); // 필요한 필드를 채워주세요.
//        given(pageService.updatePage(updatePageDto)).willReturn(1L);
//
//        mockMvc.perform(put("/update")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatePageDto)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void deletePageTest() throws Exception {
//        Long pageId = 1L;
//        String accessToken = "Bearer yourAccessToken";
//        Long userId = 1L;
//        given(jwtTokenProvider.getUserPk(accessToken)).willReturn(userId);
//        given(pageService.deletePage(pageId, userId)).willReturn(pageId);
//
//        mockMvc.perform(delete("/delete/{pageId}", pageId)
//                        .header("Authorization", accessToken))
//                .andExpect(status().isOk());
//    }
//
//
//    // 페이지 링크 테스트
//    @Test
//    public void testGetPageLink() throws Exception {
//        LinkPageDto linkPageDto = new LinkPageDto(); // 필요한 필드를 채워야 함
//        Long expectedId = 1L; // 예상되는 페이지 ID
//
//        // 서비스 호출 결과 모의 설정
//        given(pageService.linkPage(linkPageDto)).willReturn(expectedId);
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/page/link")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{ /* JSON 형식의 linkPageDto */ }"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(expectedId));
//    }
//
//    // 페이지 좋아요 테스트
//    @Test
//    public void testLikePage() throws Exception {
//        LikePageDto likePageDto = new LikePageDto(); // 필요한 필드를 채워야 함
//        String accessToken = "access_token";
//        Long expectedId = 1L; // 예상되는 페이지 ID
//
//        // JWT와 서비스 호출 결과 모의 설정
//        given(jwtTokenProvider.getUserPk(accessToken)).willReturn(1L);
//        given(pageService.likePage(likePageDto, accessToken)).willReturn(expectedId);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/page/like")
//                        .header("Authorization", "Bearer " + accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{ /* JSON 형식의 likePageDto */ }"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(expectedId));
//    }
//
//    // 페이지 좋아요 취소 테스트
//    @Test
//    public void testDislikePage() throws Exception {
//        LikePageDto likePageDto = new LikePageDto(); // 필요한 필드를 채워야 함
//        String accessToken = "access_token";
//        Long expectedId = 1L; // 예상되는 페이지 ID
//
//        // JWT와 서비스 호출 결과 모의 설정
//        given(jwtTokenProvider.getUserPk(accessToken)).willReturn(1L);
//        given(pageService.dislikePage(likePageDto, accessToken)).willReturn(expectedId);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/page/dislike")
//                        .header("Authorization", "Bearer " + accessToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{ /* JSON 형식의 likePageDto */ }"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(expectedId));
//    }
//
//    // 특정 날짜에 적은 페이지 정보 반환 테스트
//    @Test
//    public void testGetPageListByDate() throws Exception {
//        Long userId = 1L;
//        int yy = 2024, mm = 5, dd = 6;
//        LocalDate date = LocalDate.of(yy, mm, dd);
//
//        List<ResponsePageDto> expectedResponse = Arrays.asList(new ResponsePageDto(), new ResponsePageDto()); // 예시 응답, 실제로는 필요한 필드를 채워야 함.
//
//        // 서비스 호출 결과 모킹
//        given(pageService.getPageListByDate(userId, date)).willReturn(expectedResponse);
//
//        mockMvc.perform(get("/{userId}", userId)
//                        .param("yy", String.valueOf(yy))
//                        .param("mm", String.valueOf(mm))
//                        .param("dd", String.valueOf(dd))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data.length()").value(expectedResponse.size()));
//    }
//
//
//}
