
import {ResponseType} from "./responses.js";

export class HttpClient {

    post(url, body, onReceive, onFail) {

        let req = new XMLHttpRequest();
        req.open("POST", url);
        req.responseType = "text";
        req.setRequestHeader("Content-Type", "application/x-ndjson")

        req.onload = () => {

            let resp = (JSON.parse(req.responseText)) ;

            if (ResponseType.ERROR === resp.type){
                onFail(resp.message);
            } else {
                onReceive();
            }

        }

        req.send(body);
    }

}