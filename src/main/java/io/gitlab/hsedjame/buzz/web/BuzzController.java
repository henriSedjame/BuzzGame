package io.gitlab.hsedjame.buzz.web;

import io.gitlab.hsedjame.buzz.data.dto.Messages;
import io.gitlab.hsedjame.buzz.infrastructure.Emitters;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public record BuzzController(Emitters emitters) {

    @GetMapping(value = "/scores")
    public Flux<Messages.PlayerScore> scores(){
        return emitters.playerScores().asFlux();
    }

    @GetMapping(value = "/buzzes")
    public Flux<Messages.Buzz> buzzes(){
        return emitters.buzzes().asFlux();
    }

    @GetMapping(value = "/questions")
    public Flux<Messages.Question> questions(){
        return emitters.questions().asFlux();
    }

    @GetMapping(value = "/answers")
    public Flux<Messages.PlayerAnswer> answers(){
        return emitters.answers().asFlux();
    }

    @GetMapping("/start")
    public Mono<Boolean> start(){
        return emitters.gameCanStart().asMono();
    }

    @GetMapping("/end")
    public Mono<Boolean> end(){
        return emitters.gameCanEnd().asMono();
    }
}
