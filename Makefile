# Variables
CASSANDRA_VERSION=4.0.7
CASSANDRA_CONTAINER=cassandra
KEYSPACE_NAME=spring_cassandra

# Run the Cassandra container
run-cassandra:
	docker run -p 9042:9042 --rm --name $(CASSANDRA_CONTAINER) -d cassandra:$(CASSANDRA_VERSION)

# Access the Cassandra shell and create the keyspace
create-keyspace:
	docker exec -it $(CASSANDRA_CONTAINER) bash -c "cqlsh -u cassandra -p cassandra -e \"CREATE KEYSPACE $(KEYSPACE_NAME) WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};\""

# Stop the Cassandra container
stop-cassandra:
	docker stop $(CASSANDRA_CONTAINER)

ALL: run-cassandra create-keyspace