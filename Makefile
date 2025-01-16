# Variables
CASSANDRA_VERSION=4.0.7
NETWORK_NAME=cassandra-network
SEED_NAME=cassandra-seed
NODE2_NAME=cassandra-node2
NODE3_NAME=cassandra-node3
KEYSPACE_NAME=spring_cassandra
CQLSH_USER=cassandra
CQLSH_PASSWORD=cassandra

# Create Docker network
network:
	docker network create $(NETWORK_NAME)

# Start seed node
seed: network
	docker run --rm --name $(SEED_NAME) --network $(NETWORK_NAME) -d cassandra:$(CASSANDRA_VERSION)

# Start additional nodes
node2: seed
	docker run --rm --name $(NODE2_NAME) --network $(NETWORK_NAME) -e CASSANDRA_SEEDS=$(SEED_NAME) -d cassandra:$(CASSANDRA_VERSION)

node3: node2
	docker run --rm --name $(NODE3_NAME) --network $(NETWORK_NAME) -e CASSANDRA_SEEDS=$(SEED_NAME) -d cassandra:$(CASSANDRA_VERSION)

# Initialize keyspace
keyspace: seed
	docker exec -it $(SEED_NAME) bash -c \
	"cqlsh -u $(CQLSH_USER) -p $(CQLSH_PASSWORD) -e \"CREATE KEYSPACE IF NOT EXISTS $(KEYSPACE_NAME) WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};\""

# Start all nodes and setup keyspace
setup: seed node2 node3 keyspace
	@echo "Cassandra cluster setup complete!"

# Check cluster status
status: seed
	docker exec -it $(SEED_NAME) bash -c "nodetool status"

# Cleanup
clean:
	docker stop $(SEED_NAME) $(NODE2_NAME) $(NODE3_NAME) || true
	docker network rm $(NETWORK_NAME) || true

# Run Spring Boot application
run:
	./mvnw spring-boot:run


