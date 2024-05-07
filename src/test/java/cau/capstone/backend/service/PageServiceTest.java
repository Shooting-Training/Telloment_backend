package cau.capstone.backend.service;

import cau.capstone.backend.User.model.repository.UserRepository;
import cau.capstone.backend.global.security.Entity.JwtTokenProvider;
import cau.capstone.backend.page.controller.ScrapController;
import cau.capstone.backend.page.dto.request.*;
import cau.capstone.backend.page.dto.response.ResponsePageDto;
import cau.capstone.backend.page.model.Like;
import cau.capstone.backend.page.model.Page;
import cau.capstone.backend.page.model.repository.BookRepository;
import cau.capstone.backend.page.model.repository.PageRepository;
import cau.capstone.backend.page.service.PageService;
import cau.capstone.backend.page.service.ScrapService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PageServiceTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ScrapService scrapService;

    @InjectMocks
    private ScrapController scrapController;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = standaloneSetup(scrapController).build();
    }

    @Test
    public void saveScrapTest() throws Exception {
        CreateScrapDto createScrapDto = new CreateScrapDto(); // 적절히 필드 설정
        Long expectedScrapId = 1L;

        when(scrapService.saveScrap(any(CreateScrapDto.class), anyString())).thenReturn(expectedScrapId);

        mockMvc.perform(post("/create")
                        .content(objectMapper.writeValueAsString(createScrapDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("accessToken", "dummyToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(expectedScrapId));

        verify(scrapService, times(1)).saveScrap(any(CreateScrapDto.class), anyString());
    }

    @Test
    public void getScrapListTest() throws Exception {
        when(scrapService.getScrapList(anyString())).thenReturn(new ArrayList<>()); // 반환할 목록 설정

        mockMvc.perform(get("/scraplist")
                        .header("accessToken", "dummyToken"))
                .andExpect(status().isOk());

        verify(scrapService, times(1)).getScrapList(anyString());
    }

    @Test
    public void deleteScrapTest() throws Exception {
        Long scrapId = 1L;
        when(scrapService.deleteScrap(scrapId, "dummyToken")).thenReturn(scrapId);

        mockMvc.perform(delete("/unscrap/{scrapId}", scrapId)
                        .header("accessToken", "dummyToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(scrapId));

        verify(scrapService, times(1)).deleteScrap(scrapId, "dummyToken");
    }

    @Test
    public void createFromScrapTest() throws Exception {
        CreatePageFromScrapDto createPageFromScrapDto = new CreatePageFromScrapDto(); // 적절히 필드 설정
        Long expectedPageId = 1L;

        when(scrapService.createPageFromScrap(any(CreatePageFromScrapDto.class), anyString())).thenReturn(expectedPageId);

        mockMvc.perform(post("/createPage")
                        .content(objectMapper.writeValueAsString(createPageFromScrapDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("accessToken", "dummyToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(expectedPageId));

        verify(scrapService, times(1)).createPageFromScrap(any(CreatePageFromScrapDto.class), anyString());
    }
}