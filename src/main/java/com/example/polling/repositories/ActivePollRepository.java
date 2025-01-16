package com.example.polling.repositories;

import com.example.polling.entities.ActivePoll;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import java.util.UUID;

public interface ActivePollRepository extends CassandraRepository<ActivePoll, UUID> {
    @Query("INSERT INTO active_polls (pollid) VALUES (?0) USING TTL ?1")
    void insertPollWithTTL(UUID pollId, int ttlInSeconds);
}