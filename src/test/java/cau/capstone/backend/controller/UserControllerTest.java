//package cau.capstone.backend.controller;
//
//
//import cau.capstone.backend.User.controller.UserController;
//import cau.capstone.backend.User.dto.request.SearchUserDto;
//import cau.capstone.backend.User.dto.request.UpdateUserDto;
//import cau.capstone.backend.User.dto.response.ResponseSearchUserDto;
//import cau.capstone.backend.User.dto.response.ResponseSimpleUserDto;
//import cau.capstone.backend.User.model.User;
//import cau.capstone.backend.User.service.UserService;
//import cau.capstone.backend.config.TestConfig;
//import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
//import cau.capstone.backend.global.security.dto.ResponseUserDto;
//import cau.capstone.backend.global.util.MessageUtil;
//import cau.capstone.backend.global.util.api.ApiResponse;
//import cau.capstone.backend.global.util.api.ResponseCode;
//import cau.capstone.backend.page.controller.PageController;
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
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@WebMvcTest(controllers = UserController.class)
//@Import(TestConfig.class)
//public class UserControllerTest {
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    public void setup(WebApplicationContext webApplicationContext) {
//        mockMvc = standaloneSetup(userController).build();
//    }
//
//    @Test
//    public void testGetMyUserInfo() throws Exception {
//        ResponseUserDto responseUserDto = new ResponseUserDto(); // 가정한 응답 객체
//        when(userService.getMyInfo()).thenReturn(responseUserDto);
//
//        mockMvc.perform(get("/me"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$").isNotEmpty());
//
//        verify(userService, times(1)).getMyInfo();
//    }
//
//    @Test
//    public void testGetUserInfo() throws Exception {
//        String email = "test@example.com";
//        ResponseUserDto responseUserDto = new ResponseUserDto(); // 가정한 응답 객체
//        when(userService.getUserInfo(email)).thenReturn(responseUserDto);
//
//        mockMvc.perform(get("/{email}", email))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$").isNotEmpty());
//
//        verify(userService, times(1)).getUserInfo(email);
//    }
//
//    @Test
//    public void testUpdateUserInfo() throws Exception {
//        UpdateUserDto updateUserDto = new UpdateUserDto(); // 요청 객체
//        String accessToken = "access-token";
//        ResponseUserDto responseUserDto = new ResponseUserDto(); // 가정한 응답 객체
//
//        when(userService.updateUserInfo(updateUserDto, accessToken)).thenReturn(responseUserDto);
//
//        mockMvc.perform(put("/update")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("accessToken", accessToken)
//                        .content(objectMapper.writeValueAsString(updateUserDto)))
//                .andExpect(status().isOk());
//
//        verify(userService, times(1)).updateUserInfo(updateUserDto, accessToken);
//    }
//
//    // 회원 삭제 테스트
//    @Test
//    public void deleteUserTest() throws Exception {
//        String accessToken = "accessTokenSample";
//        Long expectedDeletedUserId = 1L;
//
//        when(userService.deleteUser(accessToken)).thenReturn(expectedDeletedUserId);
//
//        mockMvc.perform(delete("/delete")
//                        .header("accessToken", accessToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(expectedDeletedUserId.toString()));
//
//        verify(userService).deleteUser(accessToken);
//    }
//
//    // 회원 검색 테스트
//    @Test
//    public void searchUserTest() throws Exception {
//        String accessToken = "accessTokenSample";
//        String keyword = "test";
//        List<ResponseSearchUserDto> searchResult = new ArrayList<>();
//
//        when(userService.searchUser(accessToken, keyword)).thenReturn(searchResult);
//
//        mockMvc.perform(get("/search")
//                        .param("keyword", keyword)
//                        .header("accessToken", accessToken))
//                .andExpect(status().isOk());
//
//        verify(userService).searchUser(accessToken, keyword);
//    }
//
//    // 회원 팔로우 테스트
//    @Test
//    public void followUserTest() throws Exception {
//        String accessToken = "accessTokenSample";
//        Long followeeId = 2L;
//
//        mockMvc.perform(post("/follow/{followeeId}", followeeId)
//                        .header("accessToken", accessToken))
//                .andExpect(status().isOk());
//
//        verify(userService).followUser(accessToken, followeeId);
//    }
//
//    // 회원 팔로우 취소 테스트
//    @Test
//    public void unfollowUserTest() throws Exception {
//        String accessToken = "accessTokenSample";
//        Long followeeId = 2L;
//
//        mockMvc.perform(delete("/unfollow/{followeeId}", followeeId)
//                        .header("accessToken", accessToken))
//                .andExpect(status().isOk());
//
//        verify(userService).unfollowUser(accessToken, followeeId);
//    }
//
//}
