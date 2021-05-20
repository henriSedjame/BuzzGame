package io.gitlab.hsedjame.buzz;

import io.gitlab.hsedjame.buzz.data.dto.Messages;
import io.gitlab.hsedjame.buzz.data.dto.Responses;
import io.gitlab.hsedjame.buzz.infrastructure.Emitters;
import io.gitlab.hsedjame.buzz.infrastructure.GameState;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class BuzzApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuzzApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(R2dbcEntityTemplate template){
        return args -> {
            template.getDatabaseClient()
                    .sql("""
                    CREATE TABLE Players (
                        id VARCHAR(255)  PRIMARY KEY, 
                        name VARCHAR(255) NOT NULL UNIQUE,
                        score INT8 DEFAULT 0
                    )\
                    """)
                    .fetch()
                    .rowsUpdated()
                    .as(StepVerifier::create)
                    .expectNextCount(1)
                    .verifyComplete();
        };
    }

    @Bean
    public Emitters emitters(){
        return new Emitters(
                Sinks.one(),
                Sinks.one(),
                Sinks.many().replay().all(),
                Sinks.many().replay().all(),
                Sinks.many().multicast().directAllOrNothing(),
                Sinks.many().multicast().directBestEffort(),
                Objects.requireNonNull(getQuestons()),
                Objects.requireNonNull(getQuestons()).iterator()
        );
    }

    @Bean
    public GameState gameState() {
        return new GameState(
                new AtomicBoolean(false),
                6,
                3,
                new AtomicInteger(0)
        );
    }

    @Bean
    public Sinks.Many<Responses> responsesSink(){
        return Sinks.many().unicast().onBackpressureBuffer(new ConcurrentLinkedQueue<>());
    }

    private List<Messages.Question> getQuestons() {
        return List.of();
    }
}
