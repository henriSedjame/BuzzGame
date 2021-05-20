package io.gitlab.hsedjame.buzz.infrastructure;

import reactor.core.publisher.Sinks;
import java.time.LocalDateTime;
import java.util.List;

public record Emitters(

        Sinks.Many<String> players,
        Sinks.Many<Question> questions,
        Sinks.Many<Buzz> buzz,
        Sinks.Many<PlayerAnswer> answers

) {

    record Question(String label, List<Answer> answers){}
    record Answer(String label, boolean good){}
    record PlayerAnswer(String playerName, Answer answer){}
    record Buzz(String author, LocalDateTime time){}

}
