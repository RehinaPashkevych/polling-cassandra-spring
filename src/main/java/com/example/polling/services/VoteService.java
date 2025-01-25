package com.example.polling.services;

import com.example.polling.entities.PollVote;
import com.example.polling.repositories.ActivePollRepository;
import com.example.polling.repositories.PollResultRepository;
import com.example.polling.repositories.PollVoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteService {
    @Autowired
    private PollVoteRepository pollVoteRepository;
    @Autowired
    private PollResultRepository pollResultRepository;
    @Autowired
    private ActivePollRepository activePollRepository;

    @Transactional
    public boolean vote(PollVote vote) {
        if (!activePollRepository.existsById(vote.getPollId())) {
            throw new IllegalStateException("Poll is not active or has expired.");
        }

        if (pollVoteRepository.hasVoted(vote.getPollId(), vote.getUserId()))
            return false;

        pollVoteRepository.saveVote(vote);
        pollResultRepository.incrementVoteCount(vote.getPollId(), vote.getOption());

        return true;
    }
}
