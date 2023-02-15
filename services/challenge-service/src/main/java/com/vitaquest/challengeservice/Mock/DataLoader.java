package com.vitaquest.challengeservice.Mock;

import com.vitaquest.challengeservice.Database.Repository.ChallengeRepository;
import com.vitaquest.challengeservice.Domain.Models.Challenge;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    private final ChallengeRepository repository;

    public DataLoader(ChallengeRepository repository){

        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        Challenge c1 = Challenge.builder()
//                .title("Beweging")
//                .description("Meer bewegen begint met minder zitten. Elke dag iets meer bewegen draagt bij aan een beter balans. De gemiddelde Nederlander zit 7,5uur per dag, dat kan beter! Minder zitten op werkdagen zorgt voor een welkome onderbreking en werkt als een energizer zodat je met hernieuwde energie lekker verder kan die dag.")
//                .startDate(LocalDate.now())
//                .endDate(LocalDate.of(2022, 7, 23))
//                .Reward("Een appel")
//                .build();
//
//        Challenge c2 = Challenge.builder()
//                .title("Veerkracht")
//                .description("Meer bewegen begint met minder zitten. Elke dag iets meer bewegen draagt bij aan een beter balans. De gemiddelde Nederlander zit 7,5uur per dag, dat kan beter! Minder zitten op werkdagen zorgt voor een welkome onderbreking en werkt als een energizer zodat je met hernieuwde energie lekker verder kan die dag.")
//                .startDate(LocalDate.now())
//                .endDate(LocalDate.of(2022, 7, 23))
//                .Reward("Een appel")
//                .build();
//
//        Challenge c3 = Challenge.builder()
//                .title("Beweging")
//                .description("Meer bewegen begint met minder zitten. Elke dag iets meer bewegen draagt bij aan een beter balans. De gemiddelde Nederlander zit 7,5uur per dag, dat kan beter! Minder zitten op werkdagen zorgt voor een welkome onderbreking en werkt als een energizer zodat je met hernieuwde energie lekker verder kan die dag.")
//                .startDate(LocalDate.now())
//                .endDate(LocalDate.of(2022, 7, 23))
//                .Reward("Een appel")
//                .build();

        //repository.saveAll(List.of(c1, c2, c3));
    }
}
