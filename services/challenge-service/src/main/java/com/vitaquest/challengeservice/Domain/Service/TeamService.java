package com.vitaquest.challengeservice.Domain.Service;

import com.vitaquest.challengeservice.Authentication.IAuthenticationValidator;
import com.vitaquest.challengeservice.Database.Repository.ChallengeRepository;
import com.vitaquest.challengeservice.Database.Repository.TeamRepository;
import com.vitaquest.challengeservice.Domain.DTO.*;
import com.vitaquest.challengeservice.Domain.Models.Challenge;
import com.vitaquest.challengeservice.Domain.Models.Participant;
import com.vitaquest.challengeservice.Domain.Models.Team;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TeamService {

    private final DaprClient daprClient;
    private final ChallengeRepository challengeRepository;
    private final TeamRepository teamRepository;
    private final IAuthenticationValidator validator;
    private final SimpleDateFormat dateFormat;

    @Autowired
    public TeamService(ChallengeRepository challengeRepository, TeamRepository teamRepository, IAuthenticationValidator validator) {
        this.challengeRepository = challengeRepository;
        this.teamRepository = teamRepository;
        this.validator = validator;
        this.dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        this.daprClient = new DaprClientBuilder().build();
    }

    public Team create(CreateTeamDTO dto) {
        var team = Team.builder()
                .name(dto.getName())
                .score(dto.getScore())
                .challengeId(dto.getChallengeId())
                .participants(dto.getParticipants())
                .reward(dto.getReward());

        return teamRepository.save(team.build());
    }

    public Team read(String teamId) {
        return teamRepository.findById(teamId).orElse(null);
    }

    public List<Team> readByChallengeId(String challengeId) {
        return teamRepository.findByChallengeId(challengeId);
    }

    public Team update(UpdateTeamDTO dto) {
        var team = Team.builder()
                .id(dto.getId())
                .name(dto.getName())
                .score(dto.getScore())
                .challengeId(dto.getChallengeId())
                .participants(dto.getParticipants())
                .reward(dto.getReward());

        return teamRepository.save(team.build());
    }

    public void delete(String teamId) {
        teamRepository.deleteById(teamId);
    }

    public void join(Authentication authContext, String teamId) throws IllegalAccessException {
        Map<?, ?> claims = (Map<?, ?>) FieldUtils.readField(authContext.getPrincipal(), "claims", true);
        var team = read(teamId);
        if (team == null) return;

        var challengeId = team.getChallengeId();

        var participant = new Participant();
        participant.setName((String) claims.get("oid"));
        participant.setUserId((String) claims.get("name"));

        var allTeams = teamRepository.findByChallengeId(challengeId);

        for (Team t : allTeams) {
            var participants = t.getParticipants();
            participants.removeIf(p -> Objects.equals(p.getUserId(), participant.getUserId()));
            if (!Objects.equals(t.getId(), teamId)) return;
            participants.add(participant);
        }

        teamRepository.saveAll(allTeams);
    }

    public void addPoints(AddPointsDTO dto) {
        var team = read(dto.getTeamId());
        if (team == null) return;

        team.setScore(team.getScore() + dto.getPoints());

        teamRepository.save(team);
    }

//
//    // Creates a challenge. Throws a ParseException when an invalid date is passed.
//    public Challenge createChallenge(AddChallengeDTO DTO) throws ParseException {
//        // Todo: fill every property
//        Challenge challenge = Challenge.builder()
//                .title(DTO.getTitle())
//                .description(DTO.getDescription())
//                .startDate(dateFormat.parse(DTO.getStartDate()))
//                .endDate(dateFormat.parse(DTO.getEndDate()))
//                .activeTime(Duration.parse(DTO.getActiveTime()))
//                .Reward(DTO.getReward())
//                .build();
//        challenges.add(challenge);
//
//        return challengeRepository.save(challenge);
//    }
//
//    // Gets a specific challenge by its id.
//    public Optional<Challenge> getChallenge(String id)
//    {
//        return  challengeRepository.findById(challengeID);
//    }
//
//    // Updates a challenge. Throws a ParseException when an invalid date is passed.
//    public Challenge updateChallenge(UpdateChallengeDTO dto) throws ParseException {
//        Challenge challenge = Challenge.builder()
//                .id(dto.getId())
//                .title(dto.getTitle())
//                .description(dto.getDescription())
//                .startDate(dateFormat.parse(dto.getStartDate()))
//                .endDate(dateFormat.parse(dto.getEndDate()))
//                .activeTime(Duration.parse(dto.getActiveTime()))
//                .Reward(dto.getReward())
//                .build();
//        return challengeRepository.save(challenge);
//    }
//
//    // Deletes a challenge with specified id. Returns true if found, otherwise false.
//    public boolean deleteChallenge(String challengeID)
//    {
//        Optional<Challenge> c = challengeRepository.findById(id);
//        if(c.isPresent()){
//            challengeRepository.delete(c.get());
//            return true;
//        }
//        return false;
//    }
//
//    // Gets all challenges.
//    public Optional<List<Challenge>> getAll() {
//       return Optional.of(challengeRepository.findAll());
//    }
//
//    public Optional<List<Challenge>> getAvailable() {
//        List<Challenge> allChallenges = challengeRepository.findAll();
//        Date today = new Date();
//        List<Challenge> availableChallenges = new ArrayList<>();
//        for (Challenge challenge :
//                allChallenges) {
//            if(challenge.getStartDate().compareTo(today) <= 0 && challenge.getEndDate().compareTo(today) >= 0){
//                availableChallenges.add(challenge);
//            }
//        }
//
//        return Optional.of(availableChallenges);
//    }
//
//
//    // Find challenge by id. Returns an Optional containing null if not found.
//    public Optional<Challenge> findById(String challengeId) {
//        return challengeRepository.findById(challengeId);
//
//    }
//
//    // Create a challenge to start.
//    public UserChallenge openChallenge(String challengeId, Authentication authContext) throws IllegalAccessException {
//
//        String userId = validator.getUserId(authContext);
//        assert userId != null;
//        UserChallenge userChallenge = UserChallenge.builder()
//                .challengeId(challengeId)
//                .initiatorId(userId)
//                .status(ChallengeStatus.CREATED)
//                .build();
//        userChallenge.buddyAccept(userId);
//        return userChallengeRepository.save(userChallenge);
//    }
//
//    public UserChallenge startChallenge(String userChallengeId, Authentication authContext) throws IllegalAccessException {
//
//        String userId = validator.getUserId(authContext);
//        assert userId != null;
//        Optional<UserChallenge> optChallenge = userChallengeRepository.findById(userChallengeId);
//        UserChallenge challenge;
//
//        if(optChallenge.isPresent()){
//            challenge = optChallenge.get();
//            if(challenge.getInitiatorId().equals(userId)) {
//                challenge.setStatus(ChallengeStatus.STARTED);
//                return userChallengeRepository.save(challenge);
//            }
//        }
//        return null;
//    }
//
//    public Page<Challenge> findAll(Pageable pageable){
//        return challengeRepository.findAll(pageable);
//    }
//
//
//    public UserChallenge acceptChallenge(String userChallengeId, Authentication authContext) throws Exception {
//        // Find challenge with given id and create userchallenge object.
//        String userId = validator.getUserId(authContext);
//        assert userId != null;
//        Optional<UserChallenge> userChallenge = userChallengeRepository.findById(userChallengeId);
//        if(userChallenge.isPresent()){
//            UserChallenge challenge = userChallenge.get();
//            if(challenge.buddyAccept(userId)){
//                return userChallengeRepository.save(challenge);
//            }
//            throw new Exception("User already accepted this challenge");
//        }
//        throw new Exception("No userchallenge found with given id");
//    }
//
//    public UserChallenge inviteUser(String userChallengeId, String invitedId, Authentication authContext) throws Exception {
//        // Find challenge with given id and create userchallenge object.
//        String userId = validator.getUserId(authContext);
//        assert userId != null;
//        Optional<UserChallenge> userChallenge = userChallengeRepository.findById(userChallengeId);
//        if(userChallenge.isPresent()){
//            UserChallenge challenge = userChallenge.get();
//            if(challenge.getInitiatorId().equals(userId)){
//                challenge.inviteBuddy(invitedId);
//                return userChallengeRepository.save(challenge);
//            }
//            throw new Exception("Not possible to invite this user");
//        }
//        throw new Exception("No userchallenge found with given id");
//    }
//
//    public boolean cancelChallenge(String id, Authentication authContext) throws Exception
//    {
//
//        Optional<UserChallenge> userChallenge = userChallengeRepository.findById(id);
//        String userId = validator.getUserId(authContext);
//        assert userId != null;
//        if(userChallenge.isPresent()){
//            UserChallenge challenge = userChallenge.get();
//            if(challenge.getInitiatorId().equals(userId))
//            {
//                userChallengeRepository.delete(challenge);
//            }
//            else {
//                challenge.buddyReject(userId);
//                userChallengeRepository.save(challenge);
//            }
//            return true;
//        }
//        return false;
//    }
//
//    // Todo finish the challange.
//    public  Optional<UserChallenge> completeChallenge(String userActivityID)
//    {
//
//        Optional<UserChallenge> userChallenge= userChallengeRepository.findById(userActivityID);
//        if(userChallenge.isEmpty()){
//            return Optional.empty();
//        }
//        UserChallenge challenge = userChallenge.get();
//        challenge.setStatus(ChallengeStatus.COMPLETED);
//        daprClient.publishEvent("pubsub", "completeChallenge", challenge).block();
//        return userChallenge;
//    }
//
//    public Optional<List<UserChallenge>> GetPendingChallenges(Authentication authContext) throws IllegalAccessException
//    {
//        // find challenges where userchallenge buddies contain userId
//        String userId = validator.getUserId(authContext);
//        List<Buddy> buddyObjects = buddyRepository.findByUserId(userId);
//        Optional<List<UserChallenge>> optBuddyUserChallenges = userChallengeRepository.findUserChallengeByUserBuddysContains(buddyObjects);
//        List<UserChallenge> buddyUserChallenges = optBuddyUserChallenges.orElseGet(ArrayList::new);
//
//        // find challenges where userchallenge initiorId equals userId
//        Optional<List<UserChallenge>> optInitUserChallenges = userChallengeRepository.findUserChallengeByInitiatorId(validator.getUserId(authContext));
//        List<UserChallenge> initUserChallenges = optInitUserChallenges.orElseGet(ArrayList::new);
//
//        List<UserChallenge> openChallenges = new ArrayList<>();
//
//        for (UserChallenge challenge : buddyUserChallenges) {
//            if(challenge.getStatus() == ChallengeStatus.CREATED)
//            {
//                openChallenges.add(challenge);
//            }
//        }
//        for (UserChallenge challenge : initUserChallenges) {
//            if(challenge.getStatus() == ChallengeStatus.CREATED)
//            {
//                openChallenges.add(challenge);
//            }
//        }
//        return Optional.of(openChallenges);
//    }
//
//    public void GetAllInvited(String UserId,Authentication authContext)
//    {
//        //todo get all the invite from others...
//
//
//    }
//
//    //must find a way for checking in a group.
//    public Optional<List<UserChallenge>> getActiveByUserId(Authentication authContext) throws IllegalAccessException
//    {
//        String userId = validator.getUserId(authContext);
//        List<Buddy> buddyObjects = buddyRepository.findByUserId(userId);
//        Optional<List<UserChallenge>> optBuddyUserChallenges = userChallengeRepository.findUserChallengeByUserBuddysContains(buddyObjects);
//        List<UserChallenge> buddyUserChallenges = optBuddyUserChallenges.orElseGet(ArrayList::new);
//
//        // find challenges where userchallenge initiorId equals userId
//        Optional<List<UserChallenge>> optInitUserChallenges = userChallengeRepository.findUserChallengeByInitiatorId(validator.getUserId(authContext));
//        List<UserChallenge> initUserChallenges = optInitUserChallenges.orElseGet(ArrayList::new);
//
//        List<UserChallenge> activeChallenges = new ArrayList<>();
//
//        for (UserChallenge challenge :
//                buddyUserChallenges) {
//            if(challenge.getStatus() == ChallengeStatus.STARTED){
//                activeChallenges.add(challenge);
//            }
//        }
//        for (UserChallenge challenge :
//                initUserChallenges) {
//            if(challenge.getStatus() == ChallengeStatus.STARTED){
//                activeChallenges.add(challenge);
//            }
//        }
//        return Optional.of(activeChallenges);
//    }
}
