package com.example.polling.repositories;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.stereotype.Repository;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

@Repository
public class CustomPollResultRepositoryImpl implements CustomPollResultRepository {
    @Autowired
    private CqlTemplate cqlTemplate;

    public void incrementVoteCount(UUID uuid, int optionId) {
        SimpleStatement statement = SimpleStatement.builder(
            "UPDATE poll_results SET numofvotes  = numofvotes  + 1 WHERE poll_id = ? AND option_id = ?;")
            .addPositionalValues(uuid, optionId)
            .setConsistencyLevel(ConsistencyLevel.QUORUM).build();

        this.cqlTemplate.execute(statement);
    }
}
