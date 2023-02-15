//package com.vitaquest.badgeservice.integrations;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.vitaquest.badgeservice.Domain.DTO.AddBadgeDTO;
//import com.vitaquest.badgeservice.Domain.Models.Badge;
//import com.vitaquest.badgeservice.Domain.Service.BadgeService;
//import com.vitaquest.badgeservice.Utilities.AuthenticationContextValidator;
//import com.vitaquest.badgeservice.Utilities.IAuthenticationValidator;
//import com.vitaquest.badgeservice.integrations.config.TestSecurityConfig;
////import com.vitaquest.badgeservice.integrations.mock.BadgeData;
//import com.vitaquest.badgeservice.integrations.mock.MockAuthenticationContextValidator;
//import lombok.extern.slf4j.Slf4j;
//import org.bson.json.JsonObject;
//import org.bson.types.Binary;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.json.BasicJsonTester;
//import org.springframework.boot.test.json.JsonContent;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import static org.mockito.Mockito.when;
//
//@Slf4j
//@ActiveProfiles(value = "test")
//@SpringBootTest(classes = TestSecurityConfig.class)
//@AutoConfigureMockMvc
//public class BadgeWebTests {
//
//    // Separate testing of weblayer
//    private final BasicJsonTester json = new BasicJsonTester(this.getClass());
//
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BadgeService badgeService;
//
//
//    private static List<Badge> mockData;
//
//    @BeforeAll
//    static void setup() throws IllegalAccessException {
//        // Mock the authentication, allows for better testing of the web layer.
//        Authentication authentication = Mockito.mock(Authentication.class);
//        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//        IAuthenticationValidator validator = Mockito.mock(MockAuthenticationContextValidator.class);
//        Mockito.when(validator.isAdmin(authentication)).thenReturn(true);
//        SecurityContextHolder.setContext(securityContext);
//
//        mockData = BadgeData.getMockData();
//    }
//
//    @Test
//    public void addBadge_ShouldAddBadge_IfValidBadgeObject() throws Exception {
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file", "badge_image.png",
//                MediaType.IMAGE_PNG_VALUE,
//                this.getClass().getClassLoader().getResourceAsStream("requests/badge-vitaquest.png")
//                );
//
//        String badgeJson = getJson("requests/badge.json");
//        AddBadgeDTO dto = new ObjectMapper().readValue(badgeJson, AddBadgeDTO.class);
//        log.info(dto.getName());
//        Badge badge = Badge.builder()
//                .id(String.valueOf(UUID.randomUUID()))
//                .name(dto.getName())
//                .description(dto.getDescription())
//                .requirements(dto.getRequirements())
//                .image(new Binary(file.getBytes()))
//                .build();
//
//        when(badgeService.addBadge(file, dto)).thenReturn(badge);
//
//        MockMultipartHttpServletRequestBuilder builder =
//                MockMvcRequestBuilders.multipart("/");
//        builder.with(request -> {
//            request.setMethod("POST");
//            return request;
//        });
//
//        // Todo: fix code coverage with:  https://www.baeldung.com/sonarqube-jacoco-code-coverage
//        // Todo: Check why fails, probably because file.getBytes() returns slightly different output than file itself, resulting in mocking stub to not execute
//        // def different file for some reason.
//        MvcResult result = mockMvc.perform(builder
//                .file("image", file.getBytes())
//                .content(new ObjectMapper().writeValueAsString(dto))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//
//        String response = result.getResponse().getContentAsString();
//        JsonContent<Object> body = json.from(response);
//
//        assertThat(result.getResponse().getStatus()).isEqualTo(200);
//        assertThat(result.getResponse().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
//        assertThat(body).extractingJsonPathStringValue("$.name").isEqualTo("Piratenbadge");
//        assertThat(body).extractingJsonPathStringValue("$.description").isEqualTo("Arrrrrr");
//        assertThat(body).extractingJsonPathStringValue("$.requirements").isEqualTo("none");
//    }
//
//    @Test
//    public void addBadge_ShouldReturnBadRequest_IfImageNotPresent() throws Exception {
//
//        MockMultipartHttpServletRequestBuilder builder =
//                MockMvcRequestBuilders.multipart("/");
//        builder.with(request -> {
//            request.setMethod("POST");
//            return request;
//        });
//
//        MvcResult result = mockMvc.perform(builder
//                        .file("image", null)
//                        .content(getJson("requests/badge.json"))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//
//
//    }
//
//
//    @Test
//    public void getAllBadges_ShouldReturnBadges_IfValidRequest() throws Exception {
//        when(badgeService.getAllBadges()).thenReturn(mockData);
//
//        this.mockMvc.perform(
//                    get("/all"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().string(getJson("responses/getAllBadges.json")));
//    }
//
//    @Test
//    public void getBadgeById_ShouldReturnBadge_IfValidId() throws Exception {
//        String badgeId = "7bb8b666-6c72-4c61-bbc1-d3d3770f0e72";
//        Badge expected = mockData.get(2);
//        when(badgeService.getBadgeById(badgeId)).thenReturn(expected);
//
//        MvcResult result = this.mockMvc.perform(
//                get("/" + badgeId))
//                .andDo(print())
//                .andReturn();
//
//        String response = result.getResponse().getContentAsString();
//        JsonContent<Object> body = json.from(response);
//
//        assertThat(result.getResponse().getStatus()).isEqualTo(200);
//        assertThat(result.getResponse().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
//        assertThat(body).extractingJsonPathStringValue("$.id").isEqualTo(expected.getId());
//        assertThat(body).extractingJsonPathStringValue("$.name").isEqualTo(expected.getName());
//        assertThat(body).extractingJsonPathStringValue("$.description").isEqualTo(expected.getDescription());
//        assertThat(body).extractingJsonPathStringValue("$.requirements").isEqualTo(expected.getRequirements());
//    }
//
//    @Test
//    public void getBadgeById_ShouldReturnBadRequest_IfInvalidId() throws Exception {
//        String badgeId = "4634b8b666-6c72-4c61-bbc1-d3d3770f0e72";
//
//        when(badgeService.getBadgeById(badgeId)).thenReturn(null);
//
//       MvcResult result = this.mockMvc.perform(
//                get("/" + badgeId))
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andReturn();
//
//       assertThat(result.getResponse().getContentAsString()).isBlank();
//    }
//
//    @Test
//    public void deleteById_ShouldDeleteBadge_IfValidId() throws Exception {
//        String badgeId = "7bb8b666-6c72-4c61-bbc1-d3d3770f0e72";
//
//        when(badgeService.deleteById(badgeId)).thenReturn(true);
//
//        MvcResult result = this.mockMvc.perform(
//                delete("/" + badgeId))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        assertThat(result.getResponse().getContentAsString()).isBlank();
//    }
//
//    @Test
//    public void deleteById_ShouldReturnNotFound_IfInvalidId() throws Exception{
//        String badgeId = "4634b8b666-6c72-4c61-bbc1-d3d3770f0e72";
//
//        when(badgeService.deleteById(badgeId)).thenReturn(false);
//
//        MvcResult result = this.mockMvc.perform(
//                        delete("/" + badgeId))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andReturn();
//
//        assertThat(result.getResponse().getContentAsString()).isBlank();
//    }
//
//
//
//
//    private String getJson(String path){
//        try{
//            InputStream jsonStream = this.getClass().getClassLoader().getResourceAsStream(path);
//            assert jsonStream != null;
//            return new String(jsonStream.readAllBytes()).trim();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
