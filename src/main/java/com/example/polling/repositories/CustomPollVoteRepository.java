package com.example.polling.repositories;

import com.example.polling.entities.PollVote;

import java.util.UUID;

public interface CustomPollVoteRepository {
    boolean hasVoted(UUID pollId, UUID userId);
    void saveVote(PollVote vote);
}
