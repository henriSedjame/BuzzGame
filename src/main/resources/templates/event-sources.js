
import {Urls} from "./urls.js";
import {PlayerScoreMsg, StateChangeType} from "./messages.js";


let states = new EventSource(Urls.STATE_EVENTS_URL);


export class EventSources {

    _onPlayerScoreChanged;
    _onStartEnabled;
    _onNewQuestion;
    _onCanBuzz;
    _onAnswerReceived;
    _onBuzzReceived;
    _onError;

    constructor(onPlayerScoreChanged, onStartEnabled, onNewQuestion, onCanBuzz, onBuzz, onNewAnswer, onError) {
        this._onPlayerScoreChanged = onPlayerScoreChanged;
        this._onStartEnabled = onStartEnabled;
        this._onNewQuestion = onNewQuestion;
        this._onCanBuzz = onCanBuzz;
        this._onBuzzReceived = onBuzz;
        this._onAnswerReceived = onNewAnswer;
        this._onError = onError;
    }

    init() {

        states.onmessage = (evt) => {
            let state = JSON.parse(evt.data);

            console.log(state);

            switch (state.type) {
                case StateChangeType.GAME_START:
                    this._onStartEnabled(true, state.players);
                    break;
                case StateChangeType.GAME_END:
                    this._onStartEnabled(false, null);
                    break;
                case StateChangeType.NEW_PLAYER_SCORE:
                    this._onPlayerScoreChanged(state);
                    break;
                case StateChangeType.NEW_QUESTION:
                    this._onNewQuestion(state.message);
                    break;
                case StateChangeType.NEW_BUZZ:
                    this._onBuzzReceived(state.message);
                    break;
                case StateChangeType.NEW_ANSWER:
                    this._onAnswerReceived(state.message);
                    break;
                case StateChangeType.CAN_BUZZ:
                    this._onCanBuzz(state.message.canBuzz);
                    break;
                case StateChangeType.ERROR:
                    this._onError(state.message.message)
                    break;
            }
        }

    }

}