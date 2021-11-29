package io.gitlab.hsedjame.buzz.web;

import io.gitlab.hsedjame.buzz.data.db.PlayerRepository;
import io.gitlab.hsedjame.buzz.data.dto.Requests;
import io.gitlab.hsedjame.buzz.infrastructure.GameInfo;
import io.gitlab.hsedjame.buzz.services.BuzzService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;


@Controller
public record BuzzViewController(BuzzService service, GameInfo info, PlayerRepository repository) {

    @GetMapping("/")
    public String getIndex() {

        if (info.started().get()) return "game-started";

        /*Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                repository.existsByName(player)
                        .subscribe((Consumer<? super Boolean>) exist -> {
                            if(!exist) {
                                service.addPlayer(new Requests.AddPlayer(player))
                                        .subscribe();
                            }
                        });
            }
        }, 3000);*/

        return "index";
    }

}
