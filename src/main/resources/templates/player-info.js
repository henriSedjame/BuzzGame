
export class PlayerInfo {
    name;
    score;

    constructor(name, score) {
        this.name = name;
        this.score = score;
    }

    static fromPlayerScoreMsg(msg){
        return new PlayerInfo(msg.playerName, msg.score);
    }
}