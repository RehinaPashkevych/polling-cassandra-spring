package com.example.polling.services;

import com.example.polling.entities.Poll;
import com.example.polling.repositories.ActivePollRepository;
import com.example.polling.repositories.PollRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PollService {
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private ActivePollRepository activePollRepository;

    @Transactional
    public void createPoll(Poll poll, int durationSeconds) {
        this.activePollRepository.insertPollWithTTL(poll.getId(), durationSeconds);
        this.pollRepository.save(poll);
    }

    @Transactional
    public List<Poll> readPolls() {
        return this.pollRepository.findAll();
    }
}
