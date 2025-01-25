package com.example.polling.controllers;

import com.example.polling.entities.Poll;
import com.example.polling.services.PollService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/polls")
public class PollController {
    @Autowired
    private PollService pollService;

    @GetMapping
    public List<Poll> findAll() {
        return this.pollService.readPolls();
    }

    @PostMapping
    public Poll createPoll(@Valid @RequestBody Poll poll) {
        poll.setId(UUID.randomUUID()); 
        this.pollService.createPoll(poll);
        return poll;
    }

    @GetMapping(value = "/{id}")
    public Poll findById(@Valid @PathVariable("id") UUID pollId) {
        return this.pollService.getPoll(pollId);
    }
}
