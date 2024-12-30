package com.example.polling.entities;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.util.UUID;

@Table("active_polls")
public class ActivePoll {

    @PrimaryKey
    private UUID pollId;

    // Constructors
    public ActivePoll() {}

    public ActivePoll(UUID pollId) {
        this.pollId = pollId;
    }

    // Getters and Setters
    public UUID getPollId() {
        return pollId;
    }

    public void setPollId(UUID pollId) {
        this.pollId = pollId;
    }
}