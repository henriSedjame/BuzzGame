import {PlayerInfo} from "./player-info.js";

export class StorageService {

    storePlayer(ip, score) {
        let playerName = score.playerName;
        localStorage.setItem(ip + "-" + playerName, JSON.stringify(PlayerInfo.fromPlayerScoreMsg(score)));
    }

    getPlayer(ip, name) {
        let p = localStorage.getItem(ip + "-" + name);
        return JSON.parse(p);
    }
}