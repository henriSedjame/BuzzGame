package io.gitlab.hsedjame.buzz.infrastructure;

import io.gitlab.hsedjame.buzz.data.dto.Messages;
import io.gitlab.hsedjame.buzz.data.dto.Requests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmittersTest {

    @Autowired
    private Emitters emitters;

    @Test
    void gameStart() {
        StepVerifier.FirstStep<Messages.StateChange> stateChangeVerifier = StepVerifier.create(emitters.stateChanges().asFlux());

        List<String> players = List.of("joe", "martin", "lucie");

        emitters.gameStart(players);

        emitters.registerBuzz(new Requests.Buzz("joe"));

        emitters.stateChanges().tryEmitComplete();

        stateChangeVerifier.
                expectNext(
                        Messages.StateChange.start(players),
                        Messages.StateChange.withCanBuzz(true),
                        Messages.StateChange.withQuestion(emitters.questionList().get(0)),
                        Messages.StateChange.withCanBuzz(false),
                        Messages.StateChange.withBuzz(new Messages.Buzz("joe"))
                )
                .expectComplete()
                .verify();
    }
}