package com.example.polling.repositories;

import com.example.polling.entities.ActivePoll;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

public interface ActivePollRepository extends CrudRepository<ActivePoll, UUID> {
    @Query("INSERT INTO active_polls (pollid) VALUES (?0) USING TTL ?1")
    void insertPollWithTTL(UUID pollId, int ttlInSeconds);
}