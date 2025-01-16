# Setting Up Cassandra Cluster and Running Spring Boot Application

This guide provides step-by-step instructions to set up a Cassandra cluster using Docker and run your Spring Boot application. Follow these steps carefully to configure the cluster, create a keyspace, and verify its status.

---

## Step 1: Create a Docker Network
Create a network for Cassandra containers to communicate.

```bash
docker network create cassandra-network
```

---

## Step 2: Start the Seed Node
Start the Cassandra seed node as the primary node of the cluster.

```bash
docker run --rm --name cassandra-seed --network cassandra-network -d cassandra:4.0.7
```

---

## Step 3: Start Additional Nodes
Add more Cassandra nodes to the cluster, connecting them to the seed node.

### Node 2:
```bash
docker run --rm --name cassandra-node2 --network cassandra-network -e CASSANDRA_SEEDS=cassandra-seed -d cassandra:4.0.7
```

### Node 3:
```bash
docker run --rm --name cassandra-node3 --network cassandra-network -e CASSANDRA_SEEDS=cassandra-seed -d cassandra:4.0.7
```

---

## Step 4: Create a Keyspace
Create the required keyspace in the Cassandra database.

```bash
docker exec -it cassandra-seed bash -c "cqlsh -u cassandra -p cassandra -e \"CREATE KEYSPACE IF NOT EXISTS spring_cassandra WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};\""
```

---

## Step 5: Check Cluster Status
Verify the health and connectivity of the Cassandra cluster.

```bash
docker exec -it cassandra-seed bash -c "nodetool status"
```

---

## Step 6: Cleanup
Stop all Cassandra containers and remove the Docker network.

### Stop Containers:
```bash
docker stop cassandra-seed cassandra-node2 cassandra-node3
```

### Remove Network:
```bash
docker network rm cassandra-network
```

---

## Step 7: Run Spring Boot Application
Start your Spring Boot application.

```bash
./mvnw spring-boot:run
```

---

# Database Tables Documentation

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
