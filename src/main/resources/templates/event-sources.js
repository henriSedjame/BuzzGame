
import {Urls} from "./urls.js";
import {PlayerScoreMsg} from "./messages.js";
import {AuthService} from "./authService.js";
import {GameService, Score} from "./gameService.js";

let scoreEs = new EventSource(Urls.SCORE_EVENTS_URL);
//let answerEs = new EventSource(Urls.ANSWERS_EVENT_URL);
//let buzzEs = new EventSource(Urls.BUZZ_EVENTS_URL);
let questionEs = new EventSource(Urls.QUESTION_EVENT_URL);
let startEs = new EventSource(Urls.START_EVENT_URL);
//let endEs = new EventSource(Urls.END_EVENT_URL);
let canBuzzEs = new EventSource(Urls.CAN_BUZZ_EVENT_URL);

export class EventSources {

    _onPlayerScoreChanged;
    _onStartEnabled;
    _onNewQuestion;
    _onCanBuzz;

    constructor(onPlayerScoreChanged, onStartEnabled, onNewQuestion, onCanBuzz) {
        this._onPlayerScoreChanged = onPlayerScoreChanged;
        this._onStartEnabled = onStartEnabled;
        this._onNewQuestion = onNewQuestion;
        this._onCanBuzz = onCanBuzz;
    }

    init() {
        scoreEs.onmessage = (evt) => {
            let score = JSON.parse(evt.data);
            this._onPlayerScoreChanged(score);
        }

        startEs.onmessage = (evt) => {
            let start = JSON.parse(evt.data);
            this._onStartEnabled(start);
        }

        questionEs.onmessage = (evt) => {
            let question = JSON.parse(evt.data);
            this._onNewQuestion(question);
        }

        canBuzzEs.onmessage = (evt) => {
            let canBuzz = JSON.parse(evt.data);
            this._onCanBuzz(canBuzz);
        }
    }


}