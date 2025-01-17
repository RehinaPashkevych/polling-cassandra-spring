package com.example.polling.entities;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("poll_votes")
public class PollVote {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID pollId;

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 0)
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
