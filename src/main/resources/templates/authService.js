
import {Urls} from "./urls.js";
import {AddPlayerReq} from "./requests.js";
import {ResponseType} from "./responses.js"


export class AuthService {

    _onPlayerAdded;
    _onPlayerAddFail;

    constructor(onPlayerAdd, onPlayerAddFail) {
        this._onPlayerAdded = onPlayerAdd;
        this._onPlayerAddFail = onPlayerAddFail
    }

    login(name) {
        let body = JSON.stringify(new AddPlayerReq(name));

        let req = new XMLHttpRequest();
        req.open("POST", Urls.ADD_PLAYER_URL);
        req.responseType = "text";
        req.setRequestHeader("Content-Type", "application/x-ndjson")

        req.onload = () => {

            let resp = (JSON.parse(req.responseText)) ;

            if (ResponseType.ERROR === resp.type){
                this._onPlayerAddFail(resp.message);
            } else {
                this._onPlayerAdded(name);
            }

        }

        req.send(body);
    }

    isConnected(ip){
        let item = localStorage.getItem(ip);
        return item !== null && item !== undefined;
    }

}