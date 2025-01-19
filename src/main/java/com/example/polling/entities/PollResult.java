package com.example.polling.entities;


import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.util.UUID;

import static org.springframework.data.cassandra.core.mapping.CassandraType.Name.COUNTER;

@Table("poll_results")
public class PollResult {
    @PrimaryKeyClass
    public static class PollResultKey implements Serializable {

        @PrimaryKeyColumn(name = "poll_id", type = PrimaryKeyType.PARTITIONED)
        private UUID pollId;

        @PrimaryKeyColumn(name = "option_id", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
        private int optionId;

        // Constructors
        public PollResultKey() {}

        public PollResultKey(UUID pollId, int optionId) {
            this.pollId = pollId;
            this.optionId = optionId;
        }

        // Getters and Setters
        public UUID getPollId() {
            return pollId;
        }

        public void setPollId(UUID pollId) {
            this.pollId = pollId;
        }

        public int getOptionId() {
            return optionId;
        }

        public void setOptionId(int optionId) {
            this.optionId = optionId;
        }
    }

    @PrimaryKey
    private PollResultKey key;

    @CassandraType(type=COUNTER)
    @Column("num_of_votes")
    private long numOfVotes;

    // Constructors
    public PollResult() {}

    public PollResult(PollResultKey key, long numOfVotes) {
        this.key = key;
        this.numOfVotes = numOfVotes;
    }

    // Getters and Setters
    public PollResultKey getKey() {
        return key;
    }

    public void setKey(PollResultKey key) {
        this.key = key;
    }

    public long getNumOfVotes() {
        return numOfVotes;
    }

    public void setNumOfVotes(long numOfVotes) {
        this.numOfVotes = numOfVotes;
    }
}
