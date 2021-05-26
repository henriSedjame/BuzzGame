export class AddPlayerReq {
    name;
    constructor(name) {
        this.name = name;
    }
}

export class BuzzReq {
    playerName
}

export class AnswerReq {
    playerName;
    questionNumber;
    answerNumber
}