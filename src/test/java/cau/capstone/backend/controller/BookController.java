//package cau.capstone.backend.controller;
//
//import cau.capstone.backend.page.dto.request.AddPageToBookDto;
//import cau.capstone.backend.page.dto.request.CreateBookDto;
//import cau.capstone.backend.page.dto.request.DeletePageFromBookDto;
//import cau.capstone.backend.page.dto.response.ResponseBookDto;
//import cau.capstone.backend.page.dto.response.ResponsePageDto;
//import cau.capstone.backend.page.service.BookService;
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
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//
//@WebMvcTest(controllers = BookController.class)
//public class BookController {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BookService bookService;
//
//
//    private CreateBookDto createBookDto;
//    private AddPageToBookDto addPageToBookDto;
//    private DeletePageFromBookDto deletePageFromBookDto;
//
//    @BeforeEach
//    void setup() {
//        createBookDto = new CreateBookDto(/* 초기화 로직 */);
//        addPageToBookDto = new AddPageToBookDto(/* 초기화 로직 */);
//        deletePageFromBookDto = new DeletePageFromBookDto(/* 초기화 로직 */);
//    }
//
//    @Test
//    public void createBookTest() throws Exception {
//        // Mocking the service layer
//        given(bookService.createBook(any(CreateBookDto.class), anyString())).willReturn(1L);
//
//        mockMvc.perform(post("/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("accessToken", "dummyAccessToken")
//                        .content("{\"title\":\"Test Book\",\"author\":\"Author\"}"))
//                .andExpect(status().isOk());
//
//        // Verify that the service method was called
//        verify(bookService).createBook(any(CreateBookDto.class), anyString());
//    }
//
//    @Test
//    public void getBookListTest() throws Exception {
//        given(bookService.getBookList(anyString())).willReturn(List.of(new ResponseBookDto()));
//
//        mockMvc.perform(get("/list")
//                        .header("accessToken", "dummyAccessToken"))
//                .andExpect(status().isOk());
//
//        verify(bookService).getBookList(anyString());
//    }
//
//    @Test
//    public void deleteBookTest() throws Exception {
//        given(bookService.deleteBook(anyLong(), anyString())).willReturn(1L);
//
//        mockMvc.perform(delete("/delete/{bookId}", 1L)
//                        .header("accessToken", "dummyAccessToken"))
//                .andExpect(status().isOk());
//
//        verify(bookService).deleteBook(anyLong(), anyString());
//    }
//
//    @Test
//    public void addPageToBookTest() throws Exception {
//        given(bookService.addPageToBook(any(AddPageToBookDto.class))).willReturn(1L);
//
//        mockMvc.perform(put("/addPage")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"bookId\":1,\"pageId\":2}"))
//                .andExpect(status().isOk());
//
//        verify(bookService).addPageToBook(any(AddPageToBookDto.class));
//    }
//
//    @Test
//    public void deletePageFromBookTest() throws Exception {
//        given(bookService.deletePageFromBook(any(DeletePageFromBookDto.class), anyString())).willReturn(1L);
//
//        mockMvc.perform(put("/deletePage")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("accessToken", "dummyAccessToken")
//                        .content("{\"bookId\":1,\"pageId\":2}"))
//                .andExpect(status().isOk());
//
//        verify(bookService).deletePageFromBook(any(DeletePageFromBookDto.class), anyString());
//    }
//
//    @Test
//    public void getPageListFromBookTest() throws Exception {
//        given(bookService.getPageListFromBook(anyLong())).willReturn(List.of(new ResponsePageDto()));
//
//        mockMvc.perform(get("/pageList/{bookId}", 1L))
//                .andExpect(status().isOk());
//
//        verify(bookService).getPageListFromBook(anyLong());
//    }
//}
