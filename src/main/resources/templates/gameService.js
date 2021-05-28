
import {Urls} from "./urls.js";
import {AnswerReq, BuzzReq} from "./requests.js";


export class Score{
    player;
    score;

    constructor(player, score) {
        this.score = score;
        this.player = player;
    }
}
export class GameService {


    _httpClient;

    constructor(httpClient) {
        this._httpClient = httpClient;
    }

    buzz(name) {
        let body = JSON.stringify(new BuzzReq(name));
        this._httpClient.post(Urls.SEND_BUZZ_URL, body,
            () => {},
            (error) => { alert(error) });
    }

    answer(name, q, a) {
        let body = JSON.stringify(new AnswerReq(name, q, a));
        this._httpClient.post(Urls.SEND_ANSWER_URL, body,
            () => {},
            (error) => { alert(error) });
    }


}