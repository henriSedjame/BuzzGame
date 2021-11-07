package io.gitlab.hsedjame.buzz.infrastructure;

import io.gitlab.hsedjame.buzz.data.dto.Messages;
import io.gitlab.hsedjame.buzz.data.dto.Requests;
import io.gitlab.hsedjame.buzz.data.dto.States;
import io.gitlab.hsedjame.buzz.services.Emitters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.util.List;

@SpringBootTest
class EmittersTest {

    @Autowired
    private Emitters emitters;

    @Autowired
    private GameInfo gameInfo;

    @Test
    void gameStart() {
        StepVerifier.FirstStep<States.StateChange> stateChangeVerifier = StepVerifier.create(emitters.stateChanges().asFlux());

        List<String> players = List.of("joe", "martin", "lucie");

        gameInfo.players().addAll(players);

        emitters.onGameStarted(gameInfo);

        emitters.onBuzzRegistered(new Requests.Buzz("joe"));

        emitters.stateChanges().tryEmitComplete();

        Messages.Question question = gameInfo.currentQuestion().get().getFirst();

        stateChangeVerifier.
                expectNext(
                        States.StateChange.start(players),
                        States.StateChange.withCanBuzz(true),
                        States.StateChange.withQuestion(question),
                        States.StateChange.withCanBuzz(false),
                        States.StateChange.withBuzz(new Messages.Buzz("joe"))
                )
                .expectComplete()
                .verify();
    }
}