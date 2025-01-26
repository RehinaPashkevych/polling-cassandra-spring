# Setting Up Cassandra Cluster and Running Spring Boot Application
---
<details>  
<summary> Useful commands </summary>

1. **Build the application:**
   ```bash  
   ./mvnw clean package -DskipTests  
   ```  

2. **Start the Cassandra cluster:**
   ```bash  
   docker-compose up --build -d  
   ```  
   or
   ```bash  
   make up
   ```

3. **Access the container:**
   ```bash  
   docker exec -it spring-app-instance1 bash  
   ```  

   ```bash  
   docker exec -it cassandra-seed cqlsh    
   ```  

4. **Run the application inside spring-app-instance:**
    ```bash  
       java -jar polling.jar  
    ```

5. **Check Cluster Status:**
    ```bash
    docker exec -it cassandra-seed bash -c "nodetool status"
    ```
</details> 

<details> 

<summary> Database Tables Documentation </summary>

This document provides an overview of the tables and their respective fields in the database related to the polling system. It outlines the structure of each table and its purpose within the system.

## 1. `active_polls` Table

### Description:
The `active_polls` table stores information about the currently active polls. Each poll in this table is represented by a unique `pollId`.

### Fields:
- `poll_id (UUID)` - Primary key. A universally unique identifier (UUID) that uniquely identifies each poll.

### Example:
```sql
CREATE TABLE active_polls (
    poll_id UUID PRIMARY KEY
);
```

---

## 2. `polls` Table

### Description:
The `polls` table stores the details of all polls, including the poll ID, the question being asked, and the possible answers.

### Fields:
- `id (UUID)` - Primary key. A UUID that uniquely identifies each poll.
- `question (String)` - The question being asked in the poll.
- `answers (List<String>)` - A list of possible answers to the poll question.

### Example:
```sql
CREATE TABLE polls (
    id UUID PRIMARY KEY,
    question TEXT,
    answers list<TEXT>
);
```

---

## 3. `poll_results` Table

### Description:
The `poll_results` table stores the voting results for each option in a poll. Each entry represents a specific option in the poll, with the number of votes cast for that option.

### Fields:
- `poll_id (UUID)` - The UUID of the associated poll.
- `option_id (int)` - The ID of the option within the poll.
- `numOfVotes (long)` - The number of votes that this option has received.

### Primary Key:
The table uses a composite primary key consisting of:
- `poll_id (UUID)` - Partition key.
- `option_id (int)` - Clustering key (ordinal 0).

### Example:
```sql
CREATE TABLE poll_results (
    poll_id UUID,
    option_id int,
    num_of_votes COUNTER,
    PRIMARY KEY (poll_id, option_id)
);
```

---

## 4. `poll_votes` Table

### Description:
The `poll_votes` table tracks the users who have voted in each poll. This helps ensure that a user can only vote once per poll.

### Fields:
- `poll_id (UUID)` - The UUID of the poll.
- `user_id (UUID)` - The UUID of the user who cast the vote.

### Example:
```sql
CREATE TABLE poll_votes (
    poll_id UUID,
    user_id UUID,
    PRIMARY KEY (poll_id, user_id)
);
```

---
</details> 

<details> 

<summary> REST curl queries </summary>


Here are xamples of how to interact with the Polling API using `curl` commands. These endpoints allow you to create polls, retrieve poll details, cast votes, and fetch poll results.

## Create a Poll

Use the following command to create a new poll:

```bash
curl -X POST "http://localhost:8080/polls" -H "Content-Type: application/json" -d '{
  "question": "example-question-id",
  "answers": ["example-answer-id-1", "example-answer-id-2", "example-answer-id-3", "ex-4"],
  "ttl": 850
}'
```

- **question**: The poll's question identifier.
- **answers**: A list of possible answers.
- **ttl**: Time-to-live in seconds for the poll.

---

## Get Poll Details

Retrieve details of a specific poll using its unique ID:

```bash
curl -X GET "http://localhost:8080/polls/cc5b85fa-657c-4193-ba31-fb48d3b8ced2"
```

---

## Cast a Vote

Vote on a specific poll by providing the poll ID, user ID, and the chosen option:

```bash
curl -X POST "http://localhost:8080/poll_votes" -H "Content-Type: application/json" -d '{
  "poll_id": "cc5b85fa-657c-4193-ba31-fb48d3b8ced2",
  "user_id": "cc5b85fa-657c-4193-ba31-fb48d3b8ced2",
  "option": 1
}'
```

- **poll_id**: The unique ID of the poll.
- **user_id**: The unique ID of the user casting the vote.
- **option**: The index of the selected answer (starting from 0).

---

## Get Poll Results

Fetch the results of a poll using its unique ID:

```bash
curl -X GET "http://localhost:8080/poll_results/bc67c14e-1b5d-4768-ab4d-24d2b0e7c01e" -i
```

---

## List All Polls

Retrieve a list of all polls:

```bash
curl -X GET "http://localhost:8080/polls"
```

---

</details> 


<details> 

<summary> Test Files and Testing Process </summary>

### `test.py`
This script provides a comprehensive set of utility functions for testing the Polling API. It includes:
- **`add_random_poll()`**: Creates a poll with random questions, answers, and time-to-live (TTL).
- **`get_poll(poll_id)`**: Fetches the details of a poll using its ID.
- **`cast_vote(poll_id, user_id, option)`**: Simulates a user casting a vote for a specific poll.
- **`get_results(poll_id)`**: Retrieves the results of a poll and checks if they are finalized.

The script creates a random poll, simulates 1000 votes, and prints the results.

---

### `test-vote.py`
This script focuses on testing the voting mechanism by simulating votes under different modes:
- **`same_user`**: All votes are cast by the same user.
- **`different_users`**: Each vote is cast by a unique user.
- **`simulate_votes(poll_id, mode, n_votes, n_opts, user_id=None)`**: Simulates voting for the specified poll using the given mode and number of votes.

The script is designed to be invoked with command-line arguments:
```bash
./test-vote.py <poll_id> <mode> <n_votes> [user_id]
```
It prints the results of each voting operation.

---

## Testing Process

### Prerequisites
1. **Generate an SSH key pair**:
   ```bash
   ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
   ```
2. **Copy the public key to all application instances**:
   ```bash
   ssh-copy-id dockeruser@polling-spring-app-instance-1
   ssh-copy-id dockeruser@polling-spring-app-instance-2
   ssh-copy-id dockeruser@polling-spring-app-instance-3
   ```

### Parallel Testing
To simulate votes on multiple application instances in parallel, use the following command:
```bash
parallel -j 2 ssh -v -T dockeruser@{} "python3 app/test-vote.py d343c467-5cb4-4b14-9d95-b512cb2dc78c different_users 10 2>&1" ::: polling-spring-app-instance-2 polling-spring-app-instance-3
```

#### Explanation:
- **`parallel -j 2`**: Runs the command on two instances concurrently.
- **`ssh -v -T dockeruser@{}`**: Connects to each instance as `dockeruser`.
- **`python3 app/test-vote.py`**: Runs the `test-vote.py` script with the following arguments:
   - `poll_id`: The ID of the poll being tested.
   - `mode`: Voting mode (`different_users` in this example).
   - `n_votes`: Number of votes to cast (10 in this example).
- **`polling-spring-app-instance-2 polling-spring-app-instance-3`**: The list of instances where the command is executed.

#### Steps:
1. Generate an SSH key pair if not already created.
2. Copy the public key to the relevant instances using `ssh-copy-id`.
3. Run the `parallel` command to initiate voting simulation across instances.

This process ensures that the Polling API can handle concurrent voting operations across multiple application instances.
</details>