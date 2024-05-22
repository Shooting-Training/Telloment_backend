//package cau.capstone.backend.controller;
//
//
//import cau.capstone.backend.page.controller.PageController;
//import cau.capstone.backend.page.controller.ScrapController;
//import cau.capstone.backend.page.dto.request.CreatePageFromScrapDto;
//import cau.capstone.backend.page.dto.request.CreateScrapDto;
//import cau.capstone.backend.page.model.Scrap;
//import cau.capstone.backend.page.service.ScrapService;
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
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.ArrayList;
//
//@WebMvcTest(controllers = ScrapController.class)
//public class ScrapControllerTest {
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private ScrapService scrapService;
//
//    @InjectMocks
//    private ScrapController scrapController;
//
//    @BeforeEach
//    public void setup() {
//        objectMapper = new ObjectMapper();
//        mockMvc = standaloneSetup(scrapController).build();
//    }
//
//    @Test
//    public void saveScrapTest() throws Exception {
//        CreateScrapDto createScrapDto = new CreateScrapDto(); // 적절히 필드 설정
//        Long expectedScrapId = 1L;
//
//        when(scrapService.saveScrap(any(CreateScrapDto.class), anyString())).thenReturn(expectedScrapId);
//
//        mockMvc.perform(post("/create")
//                        .content(objectMapper.writeValueAsString(createScrapDto))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("accessToken", "dummyToken"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(expectedScrapId));
//
//        verify(scrapService, times(1)).saveScrap(any(CreateScrapDto.class), anyString());
//    }
//
//    @Test
//    public void getScrapListTest() throws Exception {
//        when(scrapService.getScrapList(anyString())).thenReturn(new ArrayList<>()); // 반환할 목록 설정
//
//        mockMvc.perform(get("/scraplist")
//                        .header("accessToken", "dummyToken"))
//                .andExpect(status().isOk());
//
//        verify(scrapService, times(1)).getScrapList(anyString());
//    }
//
//    @Test
//    public void deleteScrapTest() throws Exception {
//        Long scrapId = 1L;
//        when(scrapService.deleteScrap(scrapId, "dummyToken")).thenReturn(scrapId);
//
//        mockMvc.perform(delete("/unscrap/{scrapId}", scrapId)
//                        .header("accessToken", "dummyToken"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(scrapId));
//
//        verify(scrapService, times(1)).deleteScrap(scrapId, "dummyToken");
//    }
//
//    @Test
//    public void createFromScrapTest() throws Exception {
//        CreatePageFromScrapDto createPageFromScrapDto = new CreatePageFromScrapDto(); // 적절히 필드 설정
//        Long expectedPageId = 1L;
//
//        when(scrapService.createPageFromScrap(any(CreatePageFromScrapDto.class), anyString())).thenReturn(expectedPageId);
//
//        mockMvc.perform(post("/createPage")
//                        .content(objectMapper.writeValueAsString(createPageFromScrapDto))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("accessToken", "dummyToken"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(expectedPageId));
//
//        verify(scrapService, times(1)).createPageFromScrap(any(CreatePageFromScrapDto.class), anyString());
//    }
//}