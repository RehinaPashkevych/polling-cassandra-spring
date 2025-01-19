package com.example.polling.repositories;

import java.util.UUID;

public interface CustomPollVoteRepository {
    boolean hasVoted(UUID pollId, UUID userId);
}
