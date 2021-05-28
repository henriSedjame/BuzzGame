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

import java.util.*;
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
                Sinks.many().replay().all(),
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
                new AtomicInteger(0),
                new AtomicBoolean(false),
                new ArrayList<>()
        );
    }


    private List<Messages.Question> getQuestons() {

        return List.of(

                new Messages.Question(0, "Lequel de ces noms ne correspond pas à un langage informatique ?", 1, List.of(
                        new Messages.Answer(0, "Elm", false),
                        new Messages.Answer(1, "Rust", false),
                        new Messages.Answer(2, "Dark", true)
                )),
                new Messages.Question(1, "Dans le langage RUST quel mot clé est utilisé pour désigné une fonction ?", 2, List.of(
                        new Messages.Answer(0, "fun", false),
                        new Messages.Answer(1, "fn", true),
                        new Messages.Answer(2, "func", false)
                )),
                new Messages.Question(2, "Dans quelle version de java est apparu le mot clé 'default' ?", 1, List.of(
                        new Messages.Answer(0, "Java 11", false),
                        new Messages.Answer(1, "Java 8", true),
                        new Messages.Answer(2, "Java 14", false)
                )),
                new Messages.Question(3, "Dans le langage RUST quel mot clé est utilisé pour définir une interface ?", 2, List.of(
                        new Messages.Answer(0, "trait", true),
                        new Messages.Answer(1, "inter", false),
                        new Messages.Answer(2, "impl", false)
                )),
                new Messages.Question(4, "Dans le sigle WASI, que signifie le SI ?", 3, List.of(
                        new Messages.Answer(0, "System Interface", true),
                        new Messages.Answer(1, "Social Information", false),
                        new Messages.Answer(2, "Systeme Informatique", false)
                )),
                new Messages.Question(5, "En Kotlin quel mot clé est utilisé pour une fonction asynchrone ?", 2, List.of(
                        new Messages.Answer(0, "async", false),
                        new Messages.Answer(1, "await", false),
                        new Messages.Answer(2, "suspend", true)
                ))
        );
    }
}
