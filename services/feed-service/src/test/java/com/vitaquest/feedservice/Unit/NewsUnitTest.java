//package com.vitaquest.feedservice.Unit;
//
//import com.vitaquest.feedservice.Database.Repository.INewsRepository;
//import com.vitaquest.feedservice.Domain.DTO.AddNewsDTO;
//import com.vitaquest.feedservice.Domain.Models.News;
//import com.vitaquest.feedservice.Domain.Service.NewsService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class NewsUnitTest {
//
//    @Autowired
//    NewsService newsService;
//
//    @Autowired
//    INewsRepository newsRepository;
//
//    private News news;
//    private AddNewsDTO newsDTO;
//    private AddNewsDTO newsOneDTO;
//    private AddNewsDTO newsTwoDTO;
//    private List<News> newsList;
//    Date date = new Date();
//
//    @BeforeEach
//    public void newsData() {
//
//        String uuid = UUID.randomUUID().toString();
//        news = new News();
//        news.setId(uuid);
//        news.setTitle("title");
//        news.setDescription("desc");
//        news.setDate(date);
//
//        newsDTO = new AddNewsDTO();
//        newsDTO.setTitle("title");
//        newsDTO.setDescription("desc");
//        newsDTO.setDate(date);
//
//        newsOneDTO = new AddNewsDTO();
//        newsOneDTO.setTitle("titleOne");
//        newsOneDTO.setDescription("descOne");
//        newsOneDTO.setDate(date);
//    }
//
//    @AfterEach
//    public void clean() {
//        newsRepository.deleteAll();
//    }
//
//    @Test
//    void createShouldSave() {
//        //create news
//        News newNews = newsService.addNews(newsDTO);
//        //Pas if the new news is present
//        newsList = newsService.getAllNews();
//        //assert
//        assertNotNull(newNews);
//        assertEquals(newsDTO.getTitle(), newNews.getTitle());
//        assertEquals(newsDTO.getDescription(), newNews.getDescription());
//        assertEquals(newsDTO.getDate(), newNews.getDate());
//        assertNotNull(newsList.get(newsList.size() - 1 ));
//    }
//
//    @Test
//    void getExistingNewsById() {
//        //create testing news
//        News newNews = newsService.addNews(newsDTO);
//        //get expected news
//        News expectedNews = newsService.getNewsByID(newNews.getId());
//        //assert
//        assertNotNull(newNews);
//        assertNotNull(expectedNews);
//        assertEquals(newNews.getId(), expectedNews.getId());
//        assertEquals(newNews.getTitle(), expectedNews.getTitle());
//        assertEquals(newNews.getDescription(), expectedNews.getDescription());
//    }
//
//    @Test
//    void deleteById() {
//        // create testing news
//        News newNews = newsService.addNews(newsDTO);
//        // get the expected news
//        News expectedBooster = newsService.getNewsByID(newNews.getId());
//        //delete booster
//        newsService.deleteNewsByID(expectedBooster.getId());
//        //assert
//        assertNotNull(newNews);
//        assertNotNull(expectedBooster);
//    }
//
//    @Test
//    void getAllNews(){
//        //add news
//        newsService.addNews(newsDTO);
//        newsService.addNews(newsOneDTO);
//        //get list of news
//        newsList = newsService.getAllNews();
//        // assert to check news
//        assertEquals(2, newsList.size());
//    }
//
//
//}
