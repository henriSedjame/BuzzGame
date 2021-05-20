package io.gitlab.hsedjame.buzz.services;

import io.gitlab.hsedjame.buzz.data.dto.Requests;
import io.gitlab.hsedjame.buzz.data.dto.Responses;
import reactor.core.publisher.Mono;

public interface BuzzService {

    Mono<Responses.GameStarted> startGame();

    Mono<Responses.PlayerAdded> addPlayer(Requests.AddPlayer request);

    Mono<Responses.BuzzRegistered> addBuzz(Requests.Buzz request);

    Mono<Responses.AnswerRegistered> addAnswer(Requests.Answer request);

}
