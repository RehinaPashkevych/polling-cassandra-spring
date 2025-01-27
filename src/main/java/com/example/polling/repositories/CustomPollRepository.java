package com.example.polling.repositories;

import com.example.polling.entities.Poll;

import java.util.List;

public interface CustomPollRepository {
    List<Poll> findAll();
    void insertPoll(Poll poll);
}
