package com.example.polling.services;

import com.example.polling.entities.ActivePoll;
import com.example.polling.entities.Poll;
import com.example.polling.entities.PollResult;
import com.example.polling.repositories.ActivePollRepository;
import com.example.polling.repositories.PollRepository;
import com.example.polling.repositories.PollResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PollService {

    private final PollRepository pollRepository;
    private final ActivePollRepository activePollRepository;
    private final PollResultRepository pollResultRepository;

    public PollService(PollRepository pollRepository, ActivePollRepository activePollRepository, PollResultRepository pollResultRepository) {
        this.pollRepository = pollRepository;
        this.activePollRepository = activePollRepository;
        this.pollResultRepository = pollResultRepository;
    }

    @Transactional
    public void createPoll(Poll poll, int durationSeconds) {
        // 1. Add UUID to active_polls with TTL
        ActivePoll activePoll = new ActivePoll(poll.getId());
        activePollRepository.insertPollWithTTL(poll.getId(), durationSeconds); // TTL handling

        // 2. Add zeroed counters for poll results (increment counters)
        for (int i = 0; i < poll.getAnswers().size(); i++) {
            PollResult.PollResultKey resultKey = new PollResult.PollResultKey(poll.getId(), i);

            // Increment the counter using the custom method
           // pollResultRepository.incrementVoteCount(poll.getId(), i); // Incrementing the vote count for each answer
        }

        // 3. Add the poll to polls table
        pollRepository.save(poll);
    }
}
