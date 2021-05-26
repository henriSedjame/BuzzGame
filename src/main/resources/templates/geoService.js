
export class GeoService {

    getIp(onReceive) {
        let url = "https://api.db-ip.com/v2/free/self";
        let req = new XMLHttpRequest();
        req.open("GET",url);
        req.responseType = "text";

        req.onload = () => {
            let parse = JSON.parse(req.responseText);
            let ipAddress = parse.ipAddress;
            onReceive(ipAddress);
        }
        req.send();
    }
}