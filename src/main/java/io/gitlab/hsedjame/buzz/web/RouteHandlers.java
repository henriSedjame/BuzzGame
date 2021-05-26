package io.gitlab.hsedjame.buzz.web;

import io.gitlab.hsedjame.buzz.data.dto.Requests;
import io.gitlab.hsedjame.buzz.data.dto.Responses;
import io.gitlab.hsedjame.buzz.services.BuzzService;
import io.gitlab.hsedjame.buzz.services.exceptions.BuzzException;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public record RouteHandlers(BuzzService service) {

    public Mono<ServerResponse> startGame(ServerRequest request){
        return service.startGame()
                .flatMap(v -> ServerResponse.ok().build())
                .onErrorResume(this::onError);
    }

    public Mono<ServerResponse> addPlayer(ServerRequest request){
        return request.bodyToMono(Requests.AddPlayer.class)
                .flatMap(service::addPlayer)
                .flatMap(r -> ServerResponse.ok().bodyValue(r))
                .onErrorResume(this::onError);
    }

    public Mono<ServerResponse> addBuzz(ServerRequest request){
        return request.bodyToMono(Requests.Buzz.class)
                .flatMap(service::addBuzz)
                .flatMap(r -> ServerResponse.ok().bodyValue(r))
                .onErrorResume(this::onError);
    }

    public Mono<ServerResponse> addAnswer(ServerRequest request){
        return request.bodyToMono(Requests.Answer.class)
                .flatMap(service::addAnswer)
                .flatMap(r -> ServerResponse.ok().bodyValue(r))
                .onErrorResume(this::onError);
    }

    public <T> Mono<ServerResponse> sse(ServerRequest sr, Publisher<T> p){
        return ServerResponse.ok().bodyValue(p).onErrorResume(this::onError);
    }

    private Mono<ServerResponse> onError(Throwable error) {

        if (error instanceof BuzzException e){
            return ServerResponse.status(e.getStatus()).bodyValue(new Responses.Error(e.getMessage()));
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new Responses.Error(error.getMessage()));
    }
}
