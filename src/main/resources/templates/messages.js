export class PlayerScoreMsg {
     playerName;
     score;
     goodAnswer;
}

class BuzzMsg {
    author;
    time;
}

class PlayerAnswerMsg {
    playerName;
    answer;
}

class AnswerMsg {
    number;
    label;
    good;
}

class Question {
    number;
    label;
    points;
    answers;
}