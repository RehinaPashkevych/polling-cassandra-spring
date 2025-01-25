package com.example.polling.entities;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Table("poll_votes")
public class PollVote {
    @PrimaryKeyColumn(name = "poll_id", type = PrimaryKeyType.PARTITIONED)
    @NotNull
    @JsonProperty("poll_id")
    private UUID pollId;

    @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.CLUSTERED, ordinal = 0)
    @NotNull
    @JsonProperty("user_id")
    private UUID userId;

    @Transient
    @NotNull
    @JsonProperty("option")
    private int option;

    // Constructors
    public PollVote() {}

    public PollVote(UUID pollId, UUID userId) {
        this.pollId = pollId;
        this.userId = userId;
    }
    public PollVote(UUID pollId, UUID userId, int option) {
        this(pollId, userId);
        this.option = option;
    }

    // Getters and Setters
    public UUID getPollId() { return this.pollId; }
    public void setPollId(UUID pollId) { this.pollId = pollId; }

    public UUID getUserId() { return this.userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public int getOption() { return this.option; }
    public void setOption(int option) { this.option = option; }
}
