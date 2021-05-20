package io.gitlab.hsedjame.buzz.services;

import io.gitlab.hsedjame.buzz.data.db.Player;
import io.gitlab.hsedjame.buzz.data.db.PlayerRepository;
import io.gitlab.hsedjame.buzz.data.dto.Messages;
import io.gitlab.hsedjame.buzz.data.dto.Requests;
import io.gitlab.hsedjame.buzz.data.dto.Responses;
import io.gitlab.hsedjame.buzz.infrastructure.Emitters;
import io.gitlab.hsedjame.buzz.infrastructure.GameState;
import io.gitlab.hsedjame.buzz.services.exceptions.BuzzException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public record BuzzServiceImpl(Emitters emitters,
                              GameState gameState,
                              PlayerRepository playerRepository) implements BuzzService {

    @Override
    public Mono<Responses.GameStarted> startGame() {
        gameState.start();
        return Mono.just(new Responses.GameStarted());
    }

    @Override
    public Mono<Responses.PlayerAdded> addPlayer(Requests.AddPlayer request) {

        String name = request.name();

        return playerRepository.existsByName(name)
                .flatMap(exist -> {
                    if (exist) return Mono.error(new BuzzException.NameAlreadyUsed(name));
                    return playerRepository.save(Player.withName(name))
                            .doOnNext(p -> {
                                emitters.emitScore(p.id(), 0, Messages.AfterScoreActions.NONE);
                                if(gameState.addPlayer())
                                    emitters.enableStartingGame();
                            })
                            .map(p -> new Responses.PlayerAdded());
                });
    }

    @Override
    public Mono<Responses.BuzzRegistered> addBuzz(Requests.Buzz request) {
        emitters.registerBuzz(request);
        return Mono.just(new Responses.BuzzRegistered());
    }

    @Override
    public Mono<Responses.AnswerRegistered> addAnswer(Requests.Answer request) {
        return playerRepository.findByName(request.playerName())
                .map(p -> {
                    emitters.registerAnswer(request, p, (pl, point) ->
                         playerRepository.save(new Player(pl.id(), pl.name(), pl.score() + point))
                    );
                    return new Responses.AnswerRegistered();
                });
    }
}
