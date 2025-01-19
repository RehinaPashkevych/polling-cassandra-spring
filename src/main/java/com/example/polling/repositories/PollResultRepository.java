package com.example.polling.repositories;

import com.example.polling.entities.PollResult;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface PollResultRepository extends CassandraRepository<PollResult, PollResult.PollResultKey>, CustomPollResultRepository {}
