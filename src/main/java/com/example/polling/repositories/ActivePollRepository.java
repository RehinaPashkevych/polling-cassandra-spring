package com.example.polling.repositories;

import com.example.polling.entities.ActivePoll;
import org.springframework.data.cassandra.repository.CassandraRepository;
import java.util.UUID;

public interface ActivePollRepository extends CassandraRepository<ActivePoll, UUID>, CustomActivePollRepository {}
