package com.example.polling.repositories;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.InsertOptions;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.example.polling.entities.ActivePoll;

public class CustomActivePollRepositoryImpl implements CustomActivePollRepository {
    @Autowired
    private CassandraTemplate cassandraTemplate;

    public void insertPollWithTTL(UUID pollId, int ttlSeconds) {
        this.cassandraTemplate.insert(
            new ActivePoll(pollId),
            InsertOptions.builder()
                .consistencyLevel(ConsistencyLevel.QUORUM)
                .ttl(ttlSeconds)
                .build()
        );
    }
}
