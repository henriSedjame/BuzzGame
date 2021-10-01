package io.gitlab.hsedjame.buzz.services;

import io.gitlab.hsedjame.buzz.data.db.Player;
import io.gitlab.hsedjame.buzz.data.db.PlayerRepository;
import io.gitlab.hsedjame.buzz.data.dto.Requests;
import io.gitlab.hsedjame.buzz.data.dto.Responses;
import io.gitlab.hsedjame.buzz.infrastructure.Emitters;
import io.gitlab.hsedjame.buzz.infrastructure.GameState;
import io.gitlab.hsedjame.buzz.services.exceptions.BuzzException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static io.gitlab.hsedjame.buzz.services.Utils.applyWith;

@Service
public record BuzzServiceImpl(Emitters emitters,
                              GameState gameState,
                              PlayerRepository playerRepository,
                              R2dbcEntityTemplate entityTemplate) implements BuzzService {


    @Override
    public Mono<Responses.GameStarted> startGame() {
        gameState.start();
        return Mono.just(new Responses.GameStarted());
    }

    @Override
    public Mono<Responses.PlayerAdded> addPlayer(Requests.AddPlayer request) {

        return applyWith(request::name,
                name -> playerRepository.existsByName(name)
                        .flatMap(exist -> {
                            if (exist) return Mono.error(new BuzzException.NameAlreadyUsed(name));
                            return entityTemplate.insert(Player.withName(name))
                                    .map(p ->
                                            applyWith(() -> gameState.addPlayer(name),
                                                    added -> {
                                                        emitters.emitScore(
                                                                p,
                                                                null,
                                                                false,
                                                                gameState.players(),
                                                                gameState.minPlayers());

                                                        if (added) emitters.gameStart(gameState.players());

                                                        return new Responses.PlayerAdded();
                                                    })
                                    );
                        }));
    }

    @Override
    public Mono<Responses.BuzzRegistered> addBuzz(Requests.Buzz request) {
        return gameState.addBuzz()
                .flatMap(buzzed -> {
                    if (buzzed) {
                        emitters.registerBuzz(request);
                        return Mono.just(new Responses.BuzzRegistered());
                    }
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Responses.AnswerRegistered> addAnswer(Requests.Answer request) {
        return gameState.releaseBuzz()
                .flatMap(released ->
                        playerRepository.findByName(request.playerName())
                                .map(p -> {
                                    emitters.registerAnswer(
                                            request,
                                            p,
                                            (pl, point) -> playerRepository.save(
                                                    new Player(pl.id(), pl.name(), pl.score() + point)), gameState);

                                    return new Responses.AnswerRegistered();
                                }));
    }

}
