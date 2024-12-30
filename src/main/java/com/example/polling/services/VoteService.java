package com.example.polling.services;

import com.example.polling.entities.PollResult;
import com.example.polling.entities.PollVote;
import com.example.polling.repositories.ActivePollRepository;
import com.example.polling.repositories.PollResultRepository;
import com.example.polling.repositories.PollVoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
public class VoteService {

    private final PollVoteRepository pollVoteRepository;
    private final PollResultRepository pollResultRepository;
    private final ActivePollRepository activePollRepository;

    public VoteService(PollVoteRepository pollVoteRepository,
                       PollResultRepository pollResultRepository,
                       ActivePollRepository activePollRepository) {
        this.pollVoteRepository = pollVoteRepository;
        this.pollResultRepository = pollResultRepository;
        this.activePollRepository = activePollRepository;
    }

    @Transactional
    public boolean vote(UUID pollId, UUID userId, int optionId) {
        // Step 1: Check if the poll is active
        if (!activePollRepository.existsById(pollId)) {
            throw new IllegalStateException("Poll is not active or has expired.");
        }

        // Step 2: Check if the user has already voted in this poll
        if (pollVoteRepository.findVote(pollId, userId).isPresent()) {
            return false; // User has already voted
        }

        // Step 3: Record the user's vote in the poll_votes table
        PollVote pollVote = new PollVote(pollId, userId);
        pollVoteRepository.save(pollVote);

        // Step 4: Increment the vote counter for the selected option in poll_results
        PollResult.PollResultKey resultKey = new PollResult.PollResultKey(pollId, optionId);
        pollResultRepository.incrementVoteCount(pollId, optionId);

        return true; // Vote successfully recorded
    }
}


