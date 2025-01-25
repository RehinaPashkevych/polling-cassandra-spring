package com.example.polling.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Table("polls")
public class Poll {
    @Id
    @PrimaryKeyColumn(name = "poll_id", type = PrimaryKeyType.PARTITIONED)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Column("question")
    @NotNull
    @JsonProperty(required = true)
    private String question;

    @Column("answers")
    @NotNull
    @JsonProperty(required = true)
    private List<String> answers;

    @Transient
    @JsonProperty(required = true, access = JsonProperty.Access.WRITE_ONLY)
    @Min(value = 30)
    private int ttl;

    // Constructors
    public Poll() {}

    public Poll(UUID id, String question, List<String> answers) {
        this.id = id;
        this.question = question;
        this.answers = answers;
    }

    public Poll(UUID id, String question, List<String> answers, int ttlSeconds) {
        this(id, question, answers);
        this.ttl = ttlSeconds;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public List<String> getAnswers() { return answers; }
    public void setAnswers(List<String> answers) { this.answers = answers;}

    public int getTTL() { return this.ttl; }
    public void setTTL(int ttl) { this.ttl = ttl; }
}
