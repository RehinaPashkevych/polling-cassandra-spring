package com.example.polling.repositories;

import com.example.polling.entities.PollVote;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PollVoteRepository extends CassandraRepository<PollVote, UUID> {
    @Query("SELECT * FROM poll_votes WHERE pollid = ?0 AND userid = ?1 ALLOW FILTERING")
    Optional<PollVote> findVote(UUID pollId, UUID userId);
}

