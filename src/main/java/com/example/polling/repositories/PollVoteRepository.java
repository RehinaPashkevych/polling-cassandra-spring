package com.example.polling.repositories;

import com.example.polling.entities.PollVote;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface PollVoteRepository extends CassandraRepository<PollVote, UUID>, CustomPollVoteRepository {}
