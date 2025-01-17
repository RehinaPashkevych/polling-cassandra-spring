# Variables
CASSANDRA_VERSION=4.0.7
NETWORK_NAME=cassandra-network

SEED_NAME=cassandra-seed
NODE2_NAME=cassandra-node2
NODE3_NAME=cassandra-node3

KEYSPACE_NAME=spring_cassandra
#CQLSH_USER=cassandra
#CQLSH_PASSWORD=cassandra
REPLICATION_CONFIG="'class': 'SimpleStrategy', 'replication_factor': 3"


# Start the Cassandra container
up:
	docker-compose up -d

# Stop the Cassandra container
down:
	docker-compose down

create-keyspace:
	docker exec -it $(NODE2_NAME) cqlsh -e "CREATE KEYSPACE IF NOT EXISTS $(KEYSPACE_NAME) WITH replication = {$(REPLICATION_CONFIG)};" ;



