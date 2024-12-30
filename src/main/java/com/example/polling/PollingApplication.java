package com.example.polling;


import com.example.polling.entities.Poll;
import com.example.polling.entities.PollResult;
import com.example.polling.services.PollService;
import com.example.polling.services.ResultService;
import com.example.polling.services.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableCassandraRepositories
public class PollingApplication {

    private static final Logger log = LoggerFactory.getLogger(PollingApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PollingApplication.class, args);
    }

    @Bean
    public CommandLineRunner clr(PollService pollService, VoteService voteService, ResultService resultService) {
        return args -> {
            // Step 1: Create a poll
            UUID pollId = UUID.randomUUID();
            List<String> answers = Arrays.asList("Option A", "Option B", "Option C");
            Poll poll = new Poll(pollId, "What's your favorite color?", answers);
            int pollDurationSeconds = 300; // Poll active for 5 minutes
            pollService.createPoll(poll, pollDurationSeconds);
            log.info("Poll created: {}", poll.getQuestion());

            // Step 2: Simulate a vote
            UUID userId = UUID.randomUUID();
            boolean voteResult = voteService.vote(pollId, userId, 0);
            if (voteResult) {
                log.info("User {} successfully voted for Option 0 in poll {}", userId, pollId);
            } else {
                log.warn("User {} failed to vote in poll {}", userId, pollId);
            }

            // Step 3: Attempt to read poll results
            try {
                List<PollResult> results = resultService.getResults(pollId);
                results.forEach(result -> log.info("Option {}: {} votes", result.getKey().getOptionId(), result.getNumOfVotes()));
            } catch (RuntimeException ex) {
                log.warn("Unable to retrieve poll results: {}", ex.getMessage());
            }
        };
    }
}