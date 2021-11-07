export class PlayerScoreMsg {
     playerName;
     score;
     goodAnswer;

     constructor(name) {
         this.playerName = name;
         this.score = 0;
     }
}

export class BuzzMsg {
    author;
    time;
}

export class PlayerAnswerMsg {
    playerName;
    answer;
}

export class AnswerMsg {
    number;
    label;
    good;
}

export class Question {
    number;
    label;
    points;
    answers;
}

export class StateChange {
    type;
    canBuzz;
    message;
    players;
    requiredNbPlayers;
}

export const StateChangeType = Object.freeze(
    {
        "GAME_START" : "GAME_START",
        "GAME_END" : "GAME_END",
        "CAN_BUZZ" : "CAN_BUZZ",
        "NEW_PLAYER_SCORE" : "NEW_PLAYER_SCORE",
        "NEW_QUESTION" : "NEW_QUESTION",
        "NEW_BUZZ" : "NEW_BUZZ",
        "NEW_ANSWER" : "NEW_ANSWER"
    }
)