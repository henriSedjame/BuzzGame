package io.gitlab.hsedjame.buzz.services;

import io.gitlab.hsedjame.buzz.services.dto.Requests;
import io.gitlab.hsedjame.buzz.services.dto.Responses;

public interface BuzzService {

    Responses.PlayerAdded addPlayer(Requests.AddPlayer request);

    void buzz(Requests.Buzz request);

    void answer(Requests.Answer request);

}
