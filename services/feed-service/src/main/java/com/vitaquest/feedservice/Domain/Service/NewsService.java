package com.vitaquest.feedservice.Domain.Service;

import com.vitaquest.feedservice.Database.Repository.INewsRepository;
import com.vitaquest.feedservice.Domain.DTO.AddNewsDTO;
import com.vitaquest.feedservice.Domain.DTO.AllUserNotificationDTO;
import com.vitaquest.feedservice.Domain.DTO.UpdateNewsDTO;
import com.vitaquest.feedservice.Domain.Models.News;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    private final INewsRepository repository;
    private final DaprClient daprClient;

    public NewsService(INewsRepository repository) {
        this.repository = repository;
        this.daprClient = new DaprClientBuilder().build();
    }

    public News addNews(AddNewsDTO DTO) {
        News news = News.builder()
                .title(DTO.getTitle())
                .description(DTO.getDescription())
                .date(DTO.getDate())
                .build();

        AllUserNotificationDTO dto = AllUserNotificationDTO.builder()
                .title("New news item published")
                .message("The news item \""+news.getTitle()+"\" has been published, check it now")
                .build();
        daprClient.publishEvent("pubsub", "notifyAllUsers", dto).block();
        return repository.save(news);
    }

    public News getNewsByID(String newsId) {
        Optional<News> foundNews = repository.findById(newsId);
        if (foundNews.isEmpty()) {
            //throw not found exception
            throw new IllegalArgumentException("News not found");
        }
        return foundNews.get();
    }

    public List<News> getAllNews() {
        return repository.findAll();
    }

    public void deleteNewsByID(String newsID) {
        Optional<News> foundNews = repository.findById(newsID);
        if (foundNews.isEmpty()) {
            //throw not found exception
            throw new IllegalArgumentException("News not found");
        }
        News news = foundNews.get();
        repository.delete(news);
    }

    public News updateNews(UpdateNewsDTO DTO){
        // get existing news by id
        News existingNews = getNewsByID(DTO.getId());
        // update news fields
        existingNews.setTitle(DTO.getTitle());
        existingNews.setDescription(DTO.getDescription());
        // update news in db
        repository.save(existingNews);
        return existingNews;
    }
}
