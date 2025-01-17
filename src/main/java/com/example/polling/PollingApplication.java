package com.example.polling;


import com.example.polling.entities.Poll;
import com.example.polling.entities.PollResult;
import com.example.polling.repositories.ActivePollRepository;
import com.example.polling.repositories.PollRepository;
import com.example.polling.repositories.PollResultRepository;
import com.example.polling.repositories.PollVoteRepository;
import com.example.polling.services.PollService;
import com.example.polling.services.ResultService;
import com.example.polling.services.VoteService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.*;
@SpringBootApplication
@EnableCassandraRepositories(basePackages = "com.example.polling.repositories")
public class PollingApplication {

    @Autowired
    private PollService pollService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private ActivePollRepository activePollRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollResultRepository pollResultRepository;

    @Autowired
    private PollVoteRepository pollVoteRepository;

    public static void main(String[] args) {
        SpringApplication.run(PollingApplication.class, args);
    }

    @PostConstruct
    public void init() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Polling System!");

        while (true) {
            try {
                System.out.println("\nChoose an option:");
                System.out.println("1. Create Poll");
                System.out.println("2. Vote");
                System.out.println("3. Show Results");
                System.out.println("4. Delete all data from tables");
                System.out.println("5. List all polls");
                System.out.println("6. Exit");

                int option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1 -> createPoll(scanner);
                    case 2 -> addVote(scanner);
                    case 3 -> showResults(scanner);
                    case 4 -> deleteAllData();
                    case 5 -> printPolls();
                    case 6 -> {
                        System.out.println("Exiting the Polling System. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please choose a valid number (1, 2, 3, or 4).\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    private void createPoll(Scanner scanner) {
        try {
            System.out.print("Enter poll question: ");
            String question = scanner.nextLine();

            System.out.print("Enter options (comma-separated): ");
            String optionsInput = scanner.nextLine();
            List<String> options = Arrays.asList(optionsInput.split(","));

            if (options.isEmpty()) {
                System.out.println("Poll must have at least one option.");
                return;
            }

            System.out.print("Enter duration (seconds): ");
            int durationInput = Integer.parseInt(scanner.nextLine());


            UUID pollId = UUID.randomUUID();
            Poll poll = new Poll(pollId, question, options);
            pollService.createPoll(poll, durationInput);

            System.out.println("Poll created successfully with ID: " + pollId);
        } catch (Exception e) {
            System.out.println("Error creating poll: " + e.getMessage());
        }
    }

    private void addVote(Scanner scanner) {
        try {
            System.out.print("Enter poll ID: ");
            UUID pollId = UUID.fromString(scanner.nextLine());

            System.out.print("Enter your user ID (UUID): ");
            UUID userId = UUID.fromString(scanner.nextLine());

            System.out.print("Enter your choice (option number): ");
            int optionId = Integer.parseInt(scanner.nextLine());

            // Fetch the poll to validate the option ID
            Optional<Poll> pollOptional = pollRepository.findById(pollId);
            if (pollOptional.isEmpty()) {
                System.out.println("Poll not found. Please enter a valid poll ID.");
                return;
            }

            Poll poll = pollOptional.get();
            int numberOfOptions = poll.getAnswers().size();

            if (optionId < 0 || optionId >= numberOfOptions) {
                System.out.println("Invalid choice. Please select a number between 0 and " + (numberOfOptions - 1) + ".");
                return;
            }

            boolean result = voteService.vote(pollId, userId, optionId);

            if (result) {
                System.out.println("Your vote has been successfully recorded!");
            } else {
                System.out.println("You have already voted in this poll.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format. Please enter valid UUIDs.");
        } catch (Exception e) {
            System.out.println("Error casting vote: " + e.getMessage());
        }
    }

    private void printPolls() {
        try {
            List<Poll> polls = pollService.readPolls();
            for (int i = 0; i < polls.size(); i++) {
                System.out.println(polls.get(i).getId());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showResults(Scanner scanner) {
        try {
            System.out.print("Enter poll ID: ");
            UUID pollId = UUID.fromString(scanner.nextLine());

            List<PollResult> results = resultService.getResults(pollId);

            if (results.isEmpty()) {
                System.out.println("No results available for this poll.");
                return;
            }

            System.out.println("Results:");
            for (PollResult result : results) {
                System.out.println("Option " + result.getKey().getOptionId() + ": " + result.getNumOfVotes() + " votes");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format. Please enter a valid UUID.");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    public void deleteAllData() {
        try {
            // Delete all data from active polls
            activePollRepository.deleteAll();

            // Delete all data from poll votes
            pollVoteRepository.deleteAll();

            // Delete all data from poll results
            pollResultRepository.deleteAll();

            // Delete all data from polls
            pollRepository.deleteAll();

            System.out.println("All data has been successfully deleted from the database.");
        } catch (Exception e) {
            System.out.println("Error occurred while deleting data: " + e.getMessage());
        }
    }
}