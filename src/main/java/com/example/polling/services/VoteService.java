package com.example.polling.services;

import com.example.polling.entities.PollVote;
import com.example.polling.exceptions.ApiException;
import com.example.polling.repositories.ActivePollRepository;
import com.example.polling.repositories.PollResultRepository;
import com.example.polling.repositories.PollVoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class VoteService {
    private static final Logger logger = LoggerFactory.getLogger(VoteService.class);

    @Autowired
    private PollVoteRepository pollVoteRepository;
    @Autowired
    private PollResultRepository pollResultRepository;
    @Autowired
    private ActivePollRepository activePollRepository;

    @Transactional
    public boolean vote(PollVote vote) {
        if (!activePollRepository.existsById(vote.getPollId())) {
            String errorMessage = "Poll is not active or has expired.";
            logger.error("Error Code: POLL_NOT_ACTIVE, Message: {}", errorMessage); // Logs the error on the backend
            throw new ApiException("POLL_NOT_ACTIVE", errorMessage); // Throws error for frontend
        }

        if (pollVoteRepository.hasVoted(vote.getPollId(), vote.getUserId())) {
            String errorMessage = "User has already voted in this poll.";
            logger.warn("Error Code: ALREADY_VOTED, Message: {}", errorMessage); // Logs a warning for duplicate votes
            throw new ApiException("ALREADY_VOTED", errorMessage);
        }


        pollVoteRepository.saveVote(vote);
        pollResultRepository.incrementVoteCount(vote.getPollId(), vote.getOption());

        return true;
    }
}
