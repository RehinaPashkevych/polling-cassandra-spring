package com.example.polling.repositories;

import java.util.UUID;

public interface CustomActivePollRepository {
    void insertPollWithTTL(UUID pollId, int ttlSeconds);
}
