package com.example.polling;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.data.cassandra.core.cql.session.DefaultSessionFactory;
import com.datastax.oss.driver.api.core.CqlSession;

@Configuration
public class CassandraConfig {

    private final CqlSession session;

    public CassandraConfig(CqlSession session) {
        this.session = session;
    }

    @Bean
    public CqlTemplate cqlTemplate() {
        return new CqlTemplate(new DefaultSessionFactory(session));
    }

    @Bean
    public CassandraTemplate cassandraTemplate() {
        return new CassandraTemplate(session);
    }
}
