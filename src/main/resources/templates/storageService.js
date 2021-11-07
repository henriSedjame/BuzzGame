import {PlayerInfo} from "./player-info.js";
import {StorageKeys} from "./storage-keys.js";

export class StorageService {

    storePlayer(ip, score) {
        let playerName = score.playerName;
        localStorage.setItem(ip + "-" + playerName, JSON.stringify(PlayerInfo.fromPlayerScoreMsg(score)));
    }


    getPlayer(ip, name) {
        let p = localStorage.getItem(ip + "-" + name);
        return JSON.parse(p);
    }

    storeImgNumber(imgNum) {
        let usedImgs = getUsedImgs();

        localStorage.setItem(StorageKeys.USED_IMGS, JSON.stringify([...usedImgs, imgNum]))
    }


    getUsedImgs() {
        let usedImgs = localStorage.getItem(StorageKeys.USED_IMGS);

        if(usedImgs == null) usedImgs = [];

        return usedImgs;
    }
}