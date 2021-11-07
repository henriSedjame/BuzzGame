package io.gitlab.hsedjame.buzz.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouteDefinitions {

    @Bean
    public RouterFunction<ServerResponse> routeDefs(RouteHandlers handlers) {
        return nest(
                POST("/game").and(accept(MediaType.APPLICATION_NDJSON)),

                route(path("/start"), handlers::startGame)
                        .andRoute(path("/player"), handlers::addPlayer)
                        .andRoute(path("/answer"), handlers::addAnswer)
                        .andRoute(path("/buzz"), handlers::addBuzz)
        );
    }

}
