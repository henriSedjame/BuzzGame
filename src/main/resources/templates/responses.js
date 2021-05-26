export class Responses {
    type
}

export class ResponseError {
    type;
    message

}
export const ResponseType = Object.freeze(
    {
        "ERROR" : "ERROR",
        "GAME_STARTED" : "GAME_STARTED",
        "PLAYER_ADDED" : "PLAYER_ADDED",
        "BUZZ_REGISTERED" : "BUZZ_REGISTERED",
        "ANSWER_REGISTERED" : "ANSWER_REGISTERED"
    }
)