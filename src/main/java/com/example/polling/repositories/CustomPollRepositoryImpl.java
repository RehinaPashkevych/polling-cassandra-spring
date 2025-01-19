package com.example.polling.repositories;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.example.polling.entities.Poll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.cql.QueryOptions;
import org.springframework.data.cassandra.core.query.Query;

public class CustomPollRepositoryImpl implements CustomPollRepository {
    @Autowired
    private CassandraTemplate cassandraTemplate;

    public List<Poll> findAll() {
        Query query = Query.query().queryOptions(
            QueryOptions.builder().consistencyLevel(ConsistencyLevel.ONE).build());
        return this.cassandraTemplate.select(query, Poll.class);
    }
}
