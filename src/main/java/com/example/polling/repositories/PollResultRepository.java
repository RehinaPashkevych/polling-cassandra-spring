package com.example.polling.repositories;

import com.example.polling.entities.PollResult;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;

public interface PollResultRepository extends CassandraRepository<PollResult, PollResult.PollResultKey>, CustomPollResultRepository {
    List<PollResult> findByKeyPollId(UUID pollId);
}
