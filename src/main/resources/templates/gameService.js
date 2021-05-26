import {StorageKeys} from "./storage-keys.js";

export class Score{
    player;
    score;

    constructor(player, score) {
        this.score = score;
        this.player = player;
    }
}
export class GameService {

    _scores = [];
    _onNewPlayer;
    _onScoreChange;

    constructor(onNewPlayer, onScoreChange) {
        this._onNewPlayer = onNewPlayer;
        this._onScoreChange = onScoreChange;
    }

    addScore(score) {
        let p = this._scores.find(p => p.player === score.player);
        if (p !== null && p !== undefined){
            p.score = score.score;
            this._onScoreChange(p);
        } else {
            this._scores.push(score);
            this._onNewPlayer(score);
        }
        localStorage.setItem(StorageKeys.PLAYER_SCORE, score.score);
    }

}