package com.example.polling.services;

import com.example.polling.entities.PollResult;
import com.example.polling.repositories.ActivePollRepository;
import com.example.polling.repositories.PollResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ResultService {

    private final ActivePollRepository activePollRepository;
    private final PollResultRepository pollResultRepository;

    public ResultService(ActivePollRepository activePollRepository, PollResultRepository pollResultRepository) {
        this.activePollRepository = activePollRepository;
        this.pollResultRepository = pollResultRepository;
    }

    public List<PollResult> getResults(UUID pollId) {
        // 1. Check if poll is still active
        if (activePollRepository.existsById(pollId)) {
            throw new RuntimeException("Poll is still active. Final results are not available.");
        }

        // 2. Get results from poll_results
        return pollResultRepository.findByKeyPollId(pollId);
    }
}

