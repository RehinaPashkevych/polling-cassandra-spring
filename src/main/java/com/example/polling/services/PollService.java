package com.example.polling.services;

import com.example.polling.entities.Poll;
import com.example.polling.repositories.ActivePollRepository;
import com.example.polling.repositories.PollRepository;

import java.util.List;
import java.util.UUID;

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
    public void createPoll(Poll poll) {
        this.activePollRepository.insertPollWithTTL(poll.getId(), poll.getTTL());
        this.pollRepository.insertPoll(poll);
    }

    @Transactional
    public List<Poll> readPolls() {
        return this.pollRepository.findAll();
    }

    @Transactional
    public Poll getPoll(UUID pollId) {
        return this.pollRepository.findById(pollId).orElse(null);
    }
}
