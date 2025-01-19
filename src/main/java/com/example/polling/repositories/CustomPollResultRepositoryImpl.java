package com.example.polling.repositories;

import com.example.polling.entities.PollResult;

import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.cql.QueryOptions;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.cassandra.core.query.Update;
import org.springframework.stereotype.Repository;

import com.datastax.oss.driver.api.core.ConsistencyLevel;

@Repository
public class CustomPollResultRepositoryImpl implements CustomPollResultRepository {
    @Autowired
    private CassandraTemplate cassandraTemplate;

    public void incrementVoteCount(UUID pollId, int optionId) {
        Query query = Query.query(
            Criteria.where("poll_id").is(pollId),
            Criteria.where("option_id").is(optionId)
        ).queryOptions(QueryOptions.builder().consistencyLevel(ConsistencyLevel.QUORUM).build());
        Update update = Update.empty().increment("num_of_votes", 1);
        this.cassandraTemplate.update(query, update, PollResult.class);
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
