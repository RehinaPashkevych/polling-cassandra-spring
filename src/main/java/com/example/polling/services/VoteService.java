package com.example.polling.services;

import com.example.polling.entities.PollVote;
import com.example.polling.repositories.ActivePollRepository;
import com.example.polling.repositories.PollResultRepository;
import com.example.polling.repositories.PollVoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class VoteService {
    @Autowired
    private PollVoteRepository pollVoteRepository;
    @Autowired
    private PollResultRepository pollResultRepository;
    @Autowired
    private ActivePollRepository activePollRepository;

    @Transactional
    public boolean vote(UUID pollId, UUID userId, int optionId) {
        if (!activePollRepository.existsById(pollId)) {
            throw new IllegalStateException("Poll is not active or has expired.");
        }

        if (pollVoteRepository.hasVoted(pollId, userId))
            return false;

        pollVoteRepository.saveVote(new PollVote(pollId, userId));
        pollResultRepository.incrementVoteCount(pollId, optionId);

        return true;
    }
}


