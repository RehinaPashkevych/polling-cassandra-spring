package com.example.polling.entities;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("poll_votes")
public class PollVote {

    @PrimaryKey
    private UUID pollId;

    private UUID userId;

    // Constructors
    public PollVote() {}

    public PollVote(UUID pollId, UUID userId) {
        this.pollId = pollId;
        this.userId = userId;
    }

    // Getters and Setters
    public UUID getPollId() {
        return pollId;
    }

    public void setPollId(UUID pollId) {
        this.pollId = pollId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
