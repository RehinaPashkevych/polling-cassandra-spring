package com.example.polling.controllers;

import com.example.polling.entities.PollVote;
import com.example.polling.services.VoteService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/poll_votes")
public class PollVoteController {
    @Autowired
    private VoteService voteService;

    @PostMapping
    private PollVote castVote(@Valid @RequestBody PollVote vote) {
        this.voteService.vote(vote);
        return vote;
    }
}
