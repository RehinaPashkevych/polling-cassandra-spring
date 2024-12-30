package com.example.polling.entities;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.util.List;
import java.util.UUID;

@Table("polls")
public class Poll {

    @PrimaryKey
    private UUID id;

    private String question;

    private List<String> answers;

    // Constructors
    public Poll() {}

    public Poll(UUID id, String question, List<String> answers) {
        this.id = id;
        this.question = question;
        this.answers = answers;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }


    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
