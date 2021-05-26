
export class Urls {
    static BASE_URL = "http://localhost:8080";
    static ADD_PLAYER_URL = Urls.BASE_URL + "/game/player";
    static START_GAME_URL = Urls.BASE_URL + "/game/start";
    static SEND_ANSWER_URL = Urls.BASE_URL + "/game/answer";
    static SEND_BUZZ_URL = Urls.BASE_URL + "/game/buzz";

    static SCORE_EVENTS_URL = Urls.BASE_URL + "/events/scores";
    static BUZZ_EVENTS_URL = Urls.BASE_URL + "/events/buzzes";
    static QUESTION_EVENT_URL = Urls.BASE_URL + "/events/questions";
    static ANSWERS_EVENT_URL = Urls.BASE_URL + "/events/answers";
    static START_EVENT_URL = Urls.BASE_URL + "/events/start";
    static END_EVENT_URL = Urls.BASE_URL + "/events/end";
    static CAN_BUZZ_EVENT_URL = Urls.BASE_URL + "/events/can-buzz";
}