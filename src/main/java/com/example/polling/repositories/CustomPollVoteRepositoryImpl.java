package com.example.polling.repositories;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.example.polling.entities.PollVote;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.data.cassandra.core.cql.QueryOptions;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;

public class CustomPollVoteRepositoryImpl implements CustomPollVoteRepository {
    @Autowired
    private CassandraTemplate cassandraTemplate;

    public boolean hasVoted(UUID pollId, UUID userId) {
        Query query = Query.query(
            Criteria.where("poll_id").is(pollId),
            Criteria.where("user_id").is(userId)
        ).queryOptions(QueryOptions.builder().consistencyLevel(ConsistencyLevel.QUORUM).build());
        return this.cassandraTemplate.select(query, PollVote.class).size() > 0;
    }

    public void saveVote(PollVote vote) {
        this.cassandraTemplate.insert(vote, InsertOptions.builder()
            .consistencyLevel(ConsistencyLevel.QUORUM).build());
    }
}
