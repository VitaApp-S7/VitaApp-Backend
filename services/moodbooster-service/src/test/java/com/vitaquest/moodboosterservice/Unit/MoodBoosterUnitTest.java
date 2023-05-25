package com.vitaquest.moodboosterservice.Unit;

import com.vitaquest.moodboosterservice.API.Controller.CategoryController;
import com.vitaquest.moodboosterservice.API.Controller.MoodboosterController;
import com.vitaquest.moodboosterservice.Database.Repository.ICategoryRepository;
import com.vitaquest.moodboosterservice.Database.Repository.IMoodboosterRepository;
import com.vitaquest.moodboosterservice.Database.Repository.IUserMoodboosterRepository;
import com.vitaquest.moodboosterservice.Domain.DTO.AddMoodboosterDTO;
import com.vitaquest.moodboosterservice.Domain.DTO.UpdateMoodboosterDTO;
import com.vitaquest.moodboosterservice.Domain.Models.Moodbooster;
import com.vitaquest.moodboosterservice.Domain.Models.Status;
import com.vitaquest.moodboosterservice.Domain.Models.UserMoodbooster;
import com.vitaquest.moodboosterservice.Domain.Models.UserMoodboosterStatus;
import com.vitaquest.moodboosterservice.Domain.Service.MoodboosterService;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoodBoosterUnitTest {

    @Autowired
    MoodboosterService moodBoosterService;
    @Autowired
    IMoodboosterRepository moodBoosterRepository;

    @Autowired
    private IUserMoodboosterRepository userMoodboostersRepository;

    private Moodbooster moodBooster;
    private UserMoodbooster completedMoodBooster;
    private UserMoodbooster acceptedMoodBooster;
    private AddMoodboosterDTO moodBoosterDTO;
    private UpdateMoodboosterDTO updateMoodboosterDTO;
    private AddMoodboosterDTO moodBoosterOneDTO;
    private AddMoodboosterDTO moodBoosterTwoDTO;
    private List<Moodbooster> moodBoostersList;


    @BeforeEach
    public void moodBoostersData() {
        // Mood Booster DTO and Model used for testing
        String uuid = UUID.randomUUID().toString();
        moodBooster = new Moodbooster();
        moodBooster.setId(uuid);
        moodBooster.setDescription("Test");
        moodBooster.setTitle("Test");
        moodBooster.setStatus(Status.ACTIVE);
        moodBooster.setPoints(10);

        completedMoodBooster = new UserMoodbooster();
        List<String> userIds = new ArrayList<>();
        String userId = "1";
        userIds.add(userId);
        completedMoodBooster.setMoodbooster(moodBooster);
        completedMoodBooster.setStatus(UserMoodboosterStatus.COMPLETE);
        completedMoodBooster.setUserIds(userIds);
        userMoodboostersRepository.save(completedMoodBooster);

        acceptedMoodBooster = new UserMoodbooster();
        acceptedMoodBooster.setId(uuid);
        acceptedMoodBooster.setMoodbooster(moodBooster);
        acceptedMoodBooster.setStatus(UserMoodboosterStatus.ACCEPTED);
        acceptedMoodBooster.setUserIds(userIds);

        moodBoosterDTO = new AddMoodboosterDTO();
        moodBoosterDTO.setDescription("TestDTO");
        moodBoosterDTO.setTitle("TestDTO");
        moodBoosterDTO.setStatus(Status.ACTIVE);
        moodBoosterDTO.setPoints(20);

        updateMoodboosterDTO = new UpdateMoodboosterDTO();
        moodBoosterDTO.setDescription("TestDTO");
        moodBoosterDTO.setTitle("TestDTO");
        moodBoosterDTO.setStatus(Status.ACTIVE);
        moodBoosterDTO.setPoints(20);

        moodBoosterOneDTO = new AddMoodboosterDTO();
        moodBoosterOneDTO.setDescription("TestOneDTO");
        moodBoosterOneDTO.setTitle("TestOneDTO");
        moodBoosterOneDTO.setStatus(Status.ACTIVE);
        moodBoosterOneDTO.setPoints(40);

        moodBoosterTwoDTO = new AddMoodboosterDTO();
        moodBoosterTwoDTO.setDescription("TestOneDTO");
        moodBoosterTwoDTO.setTitle("TestOneDTO");
        moodBoosterTwoDTO.setStatus(Status.INACTIVE);
        moodBoosterTwoDTO.setPoints(40);
    }

    @AfterEach
    public void clean() {
        moodBoosterRepository.deleteAll();
    }

    @Test
    void createShouldSave() {
        // create booster
        Moodbooster newBooster = moodBoosterService.addMoodbooster(moodBoosterDTO);
        // Pass if the new moodBooster is present
        moodBoostersList = moodBoosterService.getAllMoodboosters();
        // assert
        assertNotNull(newBooster);
        assertEquals(moodBoosterDTO.getTitle(), newBooster.getTitle());
        assertEquals(moodBoosterDTO.getStatus(), newBooster.getStatus());
        assertEquals(moodBoosterDTO.getPoints(), newBooster.getPoints());
        assertEquals(moodBoosterDTO.getDescription(), newBooster.getDescription());
        assertNotNull(moodBoostersList.get(moodBoostersList.size() - 1));
    }

    @Test
    void update() {
        Moodbooster newBooster = moodBoosterService.addMoodbooster(moodBoosterDTO);
        updateMoodboosterDTO.setId(newBooster.getId());
        // create booster
        Moodbooster existingBooster = moodBoosterService.updateMoodbooster(updateMoodboosterDTO);
        // assert
        assertNotNull(existingBooster);
        assertEquals(updateMoodboosterDTO.getTitle(), existingBooster.getTitle());
        assertEquals(updateMoodboosterDTO.getDescription(), existingBooster.getDescription());
        assertEquals(updateMoodboosterDTO.getStatus(), existingBooster.getStatus());
        assertEquals(updateMoodboosterDTO.getPoints(), existingBooster.getPoints());
    }

    @Test
    void getExistingMoodBoosterById() {
        // create testing booster
        Moodbooster newBooster = moodBoosterService.addMoodbooster(moodBoosterDTO);
        // get the expected booster
        Moodbooster expectedBooster = moodBoosterService.getMoodboosterByID(newBooster.getId());
        // assert
        assertNotNull(newBooster);
        assertNotNull(expectedBooster);
        assertEquals(newBooster.getId(), expectedBooster.getId());
        assertEquals(newBooster.getStatus(), expectedBooster.getStatus());
        assertEquals(newBooster.getPoints(), expectedBooster.getPoints());
        assertEquals(newBooster.getTitle(), expectedBooster.getTitle());
        assertEquals(newBooster.getDescription(), expectedBooster.getDescription());
    }

    @Test
    void getInvalidMoodBoosterById() {
        moodBooster.setId(null);
        // cancel incorrect mood booster
        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> moodBoosterService.getMoodboosterByID(moodBooster.getId()));
        assertEquals("The given id must not be null!", exception.getMessage());
    }

    @Test
    void deleteMoodBoosterById() {
        // create testing booster
        Moodbooster newBooster = moodBoosterService.addMoodbooster(moodBoosterDTO);
        // get the expected booster
        Moodbooster expectedBooster = moodBoosterService.getMoodboosterByID(newBooster.getId());
        // delete booster
        moodBoosterService.deleteMoodboosterByID(expectedBooster.getId());
        // assert
        assertNotNull(newBooster);
        assertNotNull(expectedBooster);
    }

    @Test
    void deleteInvalidMoodBooster() {
        moodBooster.setId(null);
        // cancel incorrect mood booster
        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> moodBoosterService.deleteMoodboosterByID(moodBooster.getId()));
        assertEquals("The given id must not be null!", exception.getMessage());
    }

    @Test
    void getAllActiveMoodBoosters() {
        // add boosters
        moodBoosterService.addMoodbooster(moodBoosterDTO);
        moodBoosterService.addMoodbooster(moodBoosterOneDTO);
        // get list of all active boosters
        moodBoostersList = moodBoosterService.getAllActiveMoodboosters();
        // assert to check active mood boosters
        assertEquals(2, moodBoostersList.size());
    }

    @Test
    void getAllMoodBoosters() {
        // add boosters
        moodBoosterService.addMoodbooster(moodBoosterDTO);
        moodBoosterService.addMoodbooster(moodBoosterOneDTO);
        moodBoosterService.addMoodbooster(moodBoosterTwoDTO);
        // get list of all boosters
        moodBoostersList = moodBoosterService.getAllMoodboosters();
        // assert to check mood boosters
        assertEquals(3, moodBoostersList.size());
    }


//    @Test
//    void cancelUserMoodBooster() {
//        // cancel accepted mood booster
//        Boolean isDeleted = moodBoosterService.cancelMoodbooster(acceptedMoodBooster.getId());
//        // assert
//        assertNotNull(isDeleted);
//        assertEquals(false, isDeleted);
//    }

    @Test
    void cancelInvalidUserMoodBooster() {
        acceptedMoodBooster.setId(null);
        // cancel incorrect mood booster
        Throwable exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> moodBoosterService.cancelMoodbooster(acceptedMoodBooster.getId()));
        assertEquals("The given id must not be null!", exception.getMessage());
    }


    //TODO MOCK DAPR CLIENT
//    @Test
//    void getCompletedMoodBoosterStatus(){
//        // complete user booster
//        moodBoosterService.completeMoodbooster(completedMoodBooster.getId());
//        // assert
//        assertEquals(UserMoodboosterStatus.COMPLETE, completedMoodBooster.getStatus());
//    }

}
