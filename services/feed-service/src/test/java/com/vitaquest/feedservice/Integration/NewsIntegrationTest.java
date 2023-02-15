//package com.vitaquest.feedservice.Integration;
//
//import com.vitaquest.feedservice.API.Controller.NewsController;
//import com.vitaquest.feedservice.Domain.Models.News;
//import com.vitaquest.feedservice.Domain.Service.NewsService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Date;
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(NewsController.class)
//@AutoConfigureMockMvc
//@ContextConfiguration
//@ActiveProfiles("dev")
//public class NewsIntegrationTest {
//
//    @Autowired
//    private MockMvc mvc;
//    @MockBean
//    private NewsService newsService;
//    private News news;
//    Date date = new Date();
//
//    @BeforeEach
//    public void setup(){
//        // testing data
//        news = new News();
//        news.setId("1");
//        news.setTitle("title");
//        news.setDescription("desc");
//        news.setDate(date);
//
//
//        news = new News();
//        news.setId("2");
//        news.setTitle("titleTwo");
//        news.setDescription("descTwo");
//        news.setDate(date);
//    }
//
//    @Test
//    void getAllNews() throws Exception {
//        when(newsService.getAllNews())
//                .thenReturn(List.of(news));
//        mvc
//                .perform(get("/all"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(2));
//    }
//
////    @Test
////    void deleteNews() throws Exception {
////        when(newsService.deleteNewsByID();)
////    }
//
//}
