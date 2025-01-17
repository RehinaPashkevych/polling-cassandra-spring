package com.example.polling.repositories;


import com.example.polling.entities.Poll;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;

public interface PollRepository extends CassandraRepository<Poll, UUID> {
    List<Poll> findAll();
}
