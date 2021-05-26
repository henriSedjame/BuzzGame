
import {PlayerInfo} from "./player-info.js";

export class StorageService{

    storePlayer(ip, score){
        localStorage.setItem(ip, JSON.stringify(PlayerInfo.fromPlayerScoreMsg(score)));
    }

    getPlayer(ip) {
        let p = localStorage.getItem(ip);
        return JSON.parse(p);
    }
}