//package com.vitaquest.challengeservice.unit;
//
//import com.vitaquest.challengeservice.Authentication.AuthenticationContextValidator;
//import com.vitaquest.challengeservice.Database.Repository.ChallengeRepository;
//import com.vitaquest.challengeservice.Database.Repository.UserChallengeRepository;
//import com.vitaquest.challengeservice.Domain.DTO.AddChallengeDTO;
//import com.vitaquest.challengeservice.Domain.Models.Challenge;
//import com.vitaquest.challengeservice.Domain.Service.ChallengeService;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.Duration;
//import java.time.LocalDate;
//import java.util.Calendar;
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class ChallengeServiceTests {
//
//    private static ChallengeService challengeService;
//    private SimpleDateFormat dateFormat;
//    private Calendar calendar;
//
//    @BeforeAll
//    public void beforeAll(){
//        calendar = Calendar.getInstance();
//
//        this.dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//    }
//
//    @BeforeEach
//    public void beforeEach() throws ParseException {
//        Date currentDate = new Date();
//        Calendar c = Calendar.getInstance();
//        Challenge c1 = Challenge.builder()
//                .id("1")
//                .title("Test title 1")
//                .description("Test description 1")
//                .startDate(dateFormat.parse(currentDate.toString()))
////                .endDate(dateFormat.parse(LocalDate.of()))
//                .activeTime(Duration.parse("P1DT0H0M0.0S"))
//                .Reward("Test reward 1")
//                .build();
//    }
//
//
//    @Test
//    public void test(){
//        Duration duration = Duration.parse("P1DT0H0M0.0S");
//        System.out.println(duration.toDays());
//    }
//
//    @Test
//    public void getAllActive_ShouldReturnChallenge_IfActive(){
//        // Arrange
//
//        // Act
//
//        // Assert
//    }
//
////    @Test
////    public void createChallenge_shouldReturnValidChallengeObject(){
////        // Arrange
////        AddChallengeDTO dto = new AddChallengeDTO("c1", "description", "01-07-2000", "01-01-2023", "Reward");
////
////        // Act
////        Challenge challenge = ChallengeServiceTests.challengeService.createChallenge(dto);
////
////        // Assert
////        assertEquals(dto.getTitle(), challenge.getTitle());
////        assertEquals(dto.getDescription(), challenge.getDescription());
////        assertEquals(dto.getStartDate(), challenge.getStartDate().toString());
////        assertEquals(dto.getEndDate(), challenge.getEndDate().toString());
////        assertEquals(dto.getReward(), challenge.getReward());
////    }
//}
