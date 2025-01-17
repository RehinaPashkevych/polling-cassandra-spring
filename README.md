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

3. **Create the keyspace:**  
   Run the following in the Cassandra cluster:
   ```sql  
   CREATE KEYSPACE spring_cassandra WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};  
   ```  
   or
   ```bash  
   make create-keyspace
   ```    

4. **Access the container:**
   ```bash  
   docker exec -it spring-app-instance1 bash  
   ```  

   ```bash  
   docker exec -it cassandra-seed cqlsh    
   ```  

5. **Run the application inside spring-app-instance:**
    ```bash  
       java -jar polling-0.0.1-SNAPSHOT.jar  
    ```

6. **Check Cluster Status:**
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
- `pollId (UUID)` - Primary key. A universally unique identifier (UUID) that uniquely identifies each poll.

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
- `pollId (UUID)` - The UUID of the poll.
- `userId (UUID)` - The UUID of the user who cast the vote.

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
