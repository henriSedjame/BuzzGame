package io.gitlab.hsedjame.buzz.web;

import io.gitlab.hsedjame.buzz.data.dto.Requests;
import io.gitlab.hsedjame.buzz.data.dto.Responses;
import io.gitlab.hsedjame.buzz.services.BuzzService;
import io.gitlab.hsedjame.buzz.services.exceptions.BuzzException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public record RouteHandlers(BuzzService service) {

    public Mono<ServerResponse> startGame(ServerRequest request){
        return service.startGame()
                .flatMap(v -> ServerResponse.ok().build())
                .onErrorResume(this::onError);
    }

    public Mono<ServerResponse> addPlayer(ServerRequest request){
        return handleRequest(request, Requests.AddPlayer.class, service::addPlayer);
    }

    public Mono<ServerResponse> addBuzz(ServerRequest request){
        return handleRequest(request, Requests.Buzz.class, service::addBuzz);
    }

    public Mono<ServerResponse> addAnswer(ServerRequest request){
        return handleRequest(request, Requests.Answer.class, service::addAnswer);
    }

    /* PRIVATE METHODS */

    private <T extends Requests<T>, R extends Responses> Mono<ServerResponse> handleRequest(ServerRequest request, Class<T> reqClazz, Function<T, Mono<R>> mapper) {
        return request.bodyToMono(reqClazz)
                .flatMap(mapper)
                .flatMap(r -> ServerResponse.ok().bodyValue(r))
                .onErrorResume(this::onError);

    }

    private Mono<ServerResponse> onError(Throwable error) {

        if (error instanceof BuzzException e){
            return ServerResponse.status(e.getStatus()).bodyValue(new Responses.Error(e.getMessage()));
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new Responses.Error(error.getMessage()));
    }
}
