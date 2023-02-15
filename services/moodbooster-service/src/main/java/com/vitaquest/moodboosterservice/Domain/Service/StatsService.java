package com.vitaquest.moodboosterservice.Domain.Service;

import com.vitaquest.moodboosterservice.Database.Repository.IUserMoodboosterRepository;
import com.vitaquest.moodboosterservice.Domain.Models.UserMoodBoosterStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsService {

    private final IUserMoodboosterRepository repository;

    @Autowired
    public StatsService(IUserMoodboosterRepository repository) {
        this.repository = repository;
    }

    public List<UserMoodBoosterStats> getUserMoodboosterStats() {
        return repository.getUserMoodboosterStats();
    }
}
