export class AddPlayerReq {
    name;
    constructor(name) {
        this.name = name;
    }
}

export class BuzzReq {
    playerName;

    constructor(name) {
        this.playerName = name;
    }
}

export class AnswerReq {
    playerName;
    questionNumber;
    answerNumber;

    constructor(name, question, answer) {
        this.playerName = name;
        this.questionNumber = question;
        this.answerNumber = answer;
    }
}