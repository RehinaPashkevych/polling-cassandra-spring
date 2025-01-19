package com.example.polling.services;

import com.example.polling.entities.ActivePoll;
import com.example.polling.entities.Poll;
import com.example.polling.entities.PollResult;
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
        // 1. Add UUID to active_polls with TTL
        ActivePoll activePoll = new ActivePoll(poll.getId());
        activePollRepository.insertPollWithTTL(poll.getId(), durationSeconds); // TTL handling

        // 2. Add zeroed counters for poll results (increment counters)
        for (int i = 0; i < poll.getAnswers().size(); i++) {
            PollResult.PollResultKey resultKey = new PollResult.PollResultKey(poll.getId(), i);
        }

        // 3. Add the poll to polls table
        pollRepository.save(poll);
    }

    @Transactional
    public List<Poll> readPolls() {
        return this.pollRepository.findAll();
    }
}
