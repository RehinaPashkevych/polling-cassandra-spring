package com.example.polling.repositories;

import com.example.polling.entities.PollResult;

import java.util.UUID;
import java.util.List;

public interface CustomPollResultRepository {
    void incrementVoteCount(UUID uuid, int optionId);
    List<PollResult> findByKeyPollId(UUID pollId);
    List<PollResult> findByKeyPollIdFinal(UUID pollId);
}
