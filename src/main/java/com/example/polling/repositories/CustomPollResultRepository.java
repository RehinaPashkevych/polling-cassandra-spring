package com.example.polling.repositories;

import java.util.UUID;

public interface CustomPollResultRepository {
    void incrementVoteCount(UUID uuid, int optionId);
}
