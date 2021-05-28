
export class Urls {

    static BASE_URL = window.location.protocol + "//" + window.location.host + window.location.pathname;
    static ADD_PLAYER_URL = Urls.BASE_URL + "game/player";
    static START_GAME_URL = Urls.BASE_URL + "game/start";
    static SEND_ANSWER_URL = Urls.BASE_URL + "game/answer";
    static SEND_BUZZ_URL = Urls.BASE_URL + "game/buzz";

    static STATE_EVENTS_URL = Urls.BASE_URL + "events/states";
}