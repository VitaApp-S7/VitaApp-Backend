package com.vitaquest.moodboosterservice.Integration;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.vitaquest.moodboosterservice.API.Controller.MoodboosterController;
import com.vitaquest.moodboosterservice.Domain.Models.Moodbooster;
import com.vitaquest.moodboosterservice.Domain.Models.Status;
import com.vitaquest.moodboosterservice.Domain.Models.UserMoodbooster;
import com.vitaquest.moodboosterservice.Domain.Models.UserMoodboosterStatus;
import com.vitaquest.moodboosterservice.Domain.Service.MoodboosterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MoodboosterController.class)
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("dev")
class MoodBoosterIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private MoodboosterService moodBoosterService;

    private Moodbooster activeBooster;
    private Moodbooster inActiveBooster;
    private UserMoodbooster acceptedBooster;
    private UserMoodbooster completedBooster;

    @BeforeEach
    public void setup() {
        // testing data
        activeBooster = new Moodbooster();
        activeBooster.setId("1");
        activeBooster.setDescription("desc");
        activeBooster.setTitle("title");
        activeBooster.setStatus(Status.ACTIVE);
        activeBooster.setPoints(2);

        inActiveBooster = new Moodbooster();
        inActiveBooster.setId("2");
        inActiveBooster.setDescription("desc-1");
        inActiveBooster.setTitle("title-1");
        inActiveBooster.setStatus(Status.INACTIVE);
        inActiveBooster.setPoints(3);

        acceptedBooster = new UserMoodbooster();
        List<String> userIds = new ArrayList<>();
        String userId = "1";
        userIds.add(userId);
        acceptedBooster.setId("3");
        acceptedBooster.setMoodbooster(activeBooster);
        acceptedBooster.setStatus(UserMoodboosterStatus.ACCEPTED);
        acceptedBooster.setUserIds(userIds);

        completedBooster = new UserMoodbooster();
        userIds.add(userId);
        completedBooster.setId("4");
        completedBooster.setMoodbooster(activeBooster);
        completedBooster.setStatus(UserMoodboosterStatus.COMPLETE);
        completedBooster.setUserIds(userIds);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getActiveBoosterById() throws Exception {
        when(moodBoosterService
                .getMoodboosterByID(activeBooster.getId()))
                .thenReturn(activeBooster);
        mvc.perform(get("/" + activeBooster.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.points").value(2));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getInActiveBoosterById() throws Exception {
        when(moodBoosterService
                .getMoodboosterByID(inActiveBooster.getId()))
                .thenReturn(inActiveBooster);
        mvc.perform(get("/" + inActiveBooster.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.description").value("desc-1"))
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andExpect(jsonPath("$.title").value("title-1"))
                .andExpect(jsonPath("$.points").value(3));

    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllActiveBoosters() throws Exception {
        when(moodBoosterService.getAllActiveMoodboosters())
                .thenReturn(List.of(activeBooster));
        mvc
                .perform(get("/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllCompletedBoosters() throws Exception {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        when(moodBoosterService.getAllCompletedMoodboosters(authContext))
                .thenReturn(List.of(completedBooster));

        mvc
                .perform(get("/completed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value("4"))
                .andExpect(jsonPath("$[0].status").value("COMPLETE"))
                .andExpect(jsonPath("$[0].moodbooster.id").value("1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAcceptedBoosters() throws Exception {
        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();
        when(moodBoosterService.getAllAcceptedMoodboosters(authContext))
                .thenReturn(List.of(acceptedBooster));

        mvc
                .perform(get("/accepted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value("3"))
                .andExpect(jsonPath("$[0].status").value("ACCEPTED"))
                .andExpect(jsonPath("$[0].moodbooster.id").value("1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllBoosters() throws Exception {
        JSONArray roles = new JSONArray();
        roles.add("Role.Admins");

        when(moodBoosterService.getAllMoodboosters())
                .thenReturn(List.of(activeBooster, inActiveBooster));
        mvc
                .perform(get("/all").with(jwt().jwt(jwt -> jwt.claim("roles", roles))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }
}
