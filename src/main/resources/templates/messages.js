export class PlayerScoreMsg {
     playerName;
     score;
     goodAnswer;
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