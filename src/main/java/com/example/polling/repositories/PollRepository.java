package com.example.polling.repositories;


import com.example.polling.entities.Poll;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

public interface PollRepository extends CrudRepository<Poll, UUID> {
}