package com.vitaquest.eventservice.Unit;

import com.vitaquest.eventservice.Database.Repository.IEventRepository;
import com.vitaquest.eventservice.Domain.DTO.AddEventDTO;
import com.vitaquest.eventservice.Domain.Models.Event;
import com.vitaquest.eventservice.Domain.Service.EventService;
//import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventUnitTest {

//    @Autowired
//    EventService eventService;
//    @Autowired
//    IEventRepository eventRepository;
//
//    public AddEventDTO eventDTO;
//
////    private String generateMockBearer() {
////        return "Bearer " + Jwts.builder().claim("oid", "1").compact();
////    }
//
//    @BeforeEach
//    public void eventsData() {
//
//        List<Event> events = new ArrayList<>();
//
//        eventDTO = new AddEventDTO();
//        eventDTO.setTitle("Event title");
//        eventDTO.setDescription("Event Description");
//        eventDTO.setDate(new Date());
//        eventDTO.setUserIds(new ArrayList<String>());
//
//        eventRepository.saveAll(events);
//
//        eventService.addEvent(eventDTO);
//
//    }
//
//    @AfterEach
//    public void clean() {
//        eventRepository.deleteAll();
//    }
//
//    @Test
//    void addNewEvent() {
//        Event newEvent = eventService.addEvent(eventDTO);
//
//        assertNotNull(newEvent);
//        assertEquals(eventDTO.getTitle(), newEvent.getTitle());
//        assertEquals(eventDTO.getDescription(), newEvent.getDescription());
//        assertEquals(eventDTO.getDate().toString(), newEvent.getDate().toString());
//        assertEquals(eventDTO.getUserIds(), newEvent.getUserIds());
//    }
//
//    @Test
//    void getAllEvents(){
//        List<Event> events = eventService.getAllEvents();
//
//        assertNotNull(events);
//        assertEquals(events.size(),1);
//    }
//
//    @Test
//    void deleteEvent() throws IllegalAccessException {
//        Event newEvent = eventService.addEvent(eventDTO);
//
//        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
//
//        assertEquals(eventRepository.count(),2);
//        eventService.deleteEvent(newEvent.getId(),authContext);
//
//        assertEquals(eventRepository.count(),1);
//    }

//    @Test
//    @WithMockUser(roles = "USER",username = "Test")
//    @WithUserDetails
//    void checkForUserInEventAddUser() throws IllegalAccessException {
//        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("test");
//        System.out.println(authContext);
//        Event newEvent = eventService.addEvent(eventDTO);
//
//        eventService.CheckIfUserIsInEvent(newEvent.getId(),authContext);
//
//        assertEquals(eventDTO.getUserIds().size(), 1);
//    }


}
