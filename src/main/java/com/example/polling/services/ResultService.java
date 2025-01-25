package com.example.polling.services;

import com.example.polling.entities.PollResult;
import com.example.polling.repositories.ActivePollRepository;
import com.example.polling.repositories.PollResultRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ResultService {
    public class Results {
        boolean active;
        List<PollResult> results;

        public Results(boolean active, List<PollResult> results) {
            this.active = active;
            this.results = results;
        }

        public boolean isActive() { return this.active; }
        public List<PollResult> getResults() { return this.results; }
    }

    @Autowired
    private ActivePollRepository activePollRepository;
    @Autowired
    private PollResultRepository pollResultRepository;

    public Results getResults(UUID pollId) {
        if (activePollRepository.existsById(pollId))
            return new Results(true, pollResultRepository.findByKeyPollId(pollId));

        return new Results(false, pollResultRepository.findByKeyPollIdFinal(pollId));
    }
}

