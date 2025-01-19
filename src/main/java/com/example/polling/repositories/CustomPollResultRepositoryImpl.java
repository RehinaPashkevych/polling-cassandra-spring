package com.example.polling.repositories;

import com.example.polling.entities.PollResult;

import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.data.cassandra.core.cql.QueryOptions;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.stereotype.Repository;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

@Repository
public class CustomPollResultRepositoryImpl implements CustomPollResultRepository {
    @Autowired
    private CqlTemplate cqlTemplate;

    @Autowired
    private CassandraTemplate cassandraTemplate;

    public void incrementVoteCount(UUID uuid, int optionId) {
        SimpleStatement statement = SimpleStatement.builder(
            "UPDATE poll_results SET numofvotes  = numofvotes  + 1 WHERE poll_id = ? AND option_id = ?;")
            .addPositionalValues(uuid, optionId)
            .setConsistencyLevel(ConsistencyLevel.QUORUM)
            .build();

        this.cqlTemplate.execute(statement);
    }

    private List<PollResult> __findByKeyPollId(UUID pollId, ConsistencyLevel cl) {
        Query query = Query.query(Criteria.where("poll_id").is(pollId))
            .queryOptions(QueryOptions.builder().consistencyLevel(cl).build());
        return this.cassandraTemplate.select(query, PollResult.class);
    }

    public List<PollResult> findByKeyPollId(UUID pollId) {
        return this.__findByKeyPollId(pollId, ConsistencyLevel.ONE);
    }

    public List<PollResult> findByKeyPollIdFinal(UUID pollId) {
        return this.__findByKeyPollId(pollId, ConsistencyLevel.QUORUM);
    }
}
