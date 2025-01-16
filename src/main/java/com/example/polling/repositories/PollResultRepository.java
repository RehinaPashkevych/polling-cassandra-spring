package com.example.polling.repositories;

import com.example.polling.entities.PollResult;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PollResultRepository extends CassandraRepository<PollResult, PollResult.PollResultKey> {
    @Query("UPDATE poll_results SET numofvotes  = numofvotes  + 1 WHERE poll_id = ?0 AND option_id = ?1")
    void incrementVoteCount(UUID pollId, int optionId);

    List<PollResult> findByKeyPollId(UUID pollId);
}