package io.gitlab.hsedjame.buzz.services;

import io.gitlab.hsedjame.buzz.data.db.Player;
import io.gitlab.hsedjame.buzz.data.db.PlayerRepository;
import io.gitlab.hsedjame.buzz.data.dto.Requests;
import io.gitlab.hsedjame.buzz.data.dto.Responses;
import io.gitlab.hsedjame.buzz.infrastructure.GameInfo;
import io.gitlab.hsedjame.buzz.services.exceptions.BuzzException;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static io.gitlab.hsedjame.buzz.services.Utils.applyWith;

@Service
public record BuzzServiceImpl(Emitters emitters,
                              GameInfo gameInfo,
                              PlayerRepository playerRepository,
                              R2dbcEntityTemplate entityTemplate) implements BuzzService {


    @Override
    public Mono<Responses.GameStarted> startGame() {
        gameInfo.start();
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
                                            applyWith(() -> gameInfo.addPlayer(name),
                                                    added -> {
                                                        emitters.emitScore(
                                                                p,
                                                                null,
                                                                false,
                                                                gameInfo.players(),
                                                                gameInfo.minPlayers());

                                                        if (added) emitters.onGameStarted(gameInfo);

                                                        return new Responses.PlayerAdded();
                                                    })
                                    );
                        }));
    }

    @Override
    public Mono<Responses.BuzzRegistered> addBuzz(Requests.Buzz request) {
        return gameInfo.addBuzz()
                .flatMap(buzzed -> {
                    if (buzzed) {
                        emitters.onBuzzRegistered(request);
                        return Mono.just(new Responses.BuzzRegistered());
                    }
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Responses.AnswerRegistered> addAnswer(Requests.Answer request) {
        /*
        * Release buzz
        * Find the player who gives the answer
        * And defer the answer registration to emitters
        * Before return the AnswerRegistered Response
        */
        gameInfo.releaseBuzz()

                .subscribe(released ->
                        playerRepository.findByName(request.playerName())
                                .subscribe(p ->
                                    emitters.onAnswerRegistered(request, p,
                                            (pl, point) -> playerRepository.save(
                                                    new Player(pl.id(), pl.name(), pl.score() + point)), gameInfo)
                                ));

        return Mono.just( new Responses.AnswerRegistered());
    }

}
