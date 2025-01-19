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
    @Autowired
    private ActivePollRepository activePollRepository;
    @Autowired
    private PollResultRepository pollResultRepository;

    public List<PollResult> getResults(UUID pollId) {
        if (activePollRepository.existsById(pollId)) {
            System.out.println("Poll is still active. Results might be inaccurate.");
            return pollResultRepository.findByKeyPollId(pollId);
        }

        return pollResultRepository.findByKeyPollIdFinal(pollId);
    }
}

