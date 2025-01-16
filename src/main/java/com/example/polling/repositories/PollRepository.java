package com.example.polling.repositories;


import com.example.polling.entities.Poll;
import org.springframework.data.cassandra.repository.CassandraRepository;
import java.util.UUID;

public interface PollRepository extends CassandraRepository<Poll, UUID> {
}