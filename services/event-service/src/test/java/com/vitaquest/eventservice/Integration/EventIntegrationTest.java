package com.vitaquest.eventservice.Integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitaquest.eventservice.API.Controller.EventController;
import com.vitaquest.eventservice.Domain.DTO.AddEventDTO;
import com.vitaquest.eventservice.Domain.Models.Event;
import com.vitaquest.eventservice.Domain.Service.EventService;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static groovy.json.JsonOutput.toJson;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EventController.class)
@AutoConfigureMockMvc
@ContextConfiguration
@AutoConfigureJsonTesters
@ActiveProfiles("dev")
class EventIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    private Event event;

//    private Event inActiveBooster;
//    private UserMoodbooster acceptedBooster;
//    private UserMoodbooster completedBooster;

    @BeforeEach
    public void setup() {
        // testing data
        event = new Event();
        event.setId("1");
        event.setDescription("desc");
        event.setTitle("title");
        event.setUserIds(new ArrayList<String>());
    }

    private String generateMockBearer() {
        return "Bearer " + Jwts.builder().claim("oid", "1").compact();
    }

    @Test
    void newTest(){
        assertTrue(true);
    }

    @Test
    @WithMockUser(roles = "USER",username = "Test")
    public void getAllEvents() throws Exception {

        when(eventService.getAllEvents())
                .thenReturn(List.of(event));

        mockMvc.perform(get("/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

//    @Test
//    @WithMockUser(roles = "ADMIN",username = "Test")
//    public void putNewEvent() throws Exception {
//        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
//
//        String exampleCourseJson = "{\"title\":\"TITLE\",\"description\":\"DESC\",\"userIds\":[\"1\",\"2\"]}";
//
//        AddEventDTO newEvent = new AddEventDTO();
//        newEvent.setTitle("TITLE");
//        newEvent.setDescription("DESC");
//        newEvent.setUserIds(new ArrayList<String>());
//
//        when(eventService.addEvent(newEvent))
//                .thenReturn(event);
//
//        mockMvc.perform(post("/add")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(exampleCourseJson)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser(roles = "USER",username = "Test")
//    public void checkUserInEvent() throws Exception {
//        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
//        when(eventService.CheckIfUserIsInEvent(event.getId(),authContext))
//                .thenReturn(event);
//
//        mockMvc.perform(put("/check-user/" + event.getId()))
//                .andDo(print())
//                .andExpect(status().isOk());
//
//    }



}