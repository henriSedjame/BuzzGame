import {AuthService} from "./authService.js";
import {GameService, Score} from "./gameService.js";
import {EventSources} from "./event-sources.js";
import {StorageService} from "./storageService.js";
import {PlayerInfo} from "./player-info.js";
import {GeoService} from "./geoService.js";


/* Variables globales */
let name = "";
let nbPlayers = 0;

const colorRed = "#FF0000FF";

/* Html Elements */
let registrationBloc = document.getElementById("registrationBloc");
let playerNameInput = document.getElementById("playerNameInput");
let playerNameBtn = document.getElementById("playerNameBtn");
let playersBloc = document.getElementById("players");
let buzzButton = document.getElementById("buzzButton");
let buzzButtonContainer = document.getElementById("buzzButtonContainer");

let questionLabel = document.getElementById("questionLabel");
let answer1 = document.getElementById("answer_1");
let answer2 = document.getElementById("answer_1");
let answer3 = document.getElementById("answer_1");



/* Services */
let storageService = new StorageService();

let geoService = new GeoService();

let authService = new AuthService(
    (player) => {
        registrationBloc.style.display = "none";
        alert("Bienvenue " + player + " !!!");
        name = "";
    },
    msg => {
        alert(msg);
        name = "";
        playerNameInput.value = "";
    }
);

let gameService = new GameService(
    score => {
    },
    score => {
    }
);

geoService.getIp(
    ip => {
        let eventSources = new EventSources(
            score => {
                if (authService.isConnected(ip)) {
                    let player = storageService.getPlayer(ip);
                    let pScore = player.score;
                    if (player.name === score.playerName && pScore !== score.score) {
                        let sView = document.getElementById("SCORE_" + player.name);
                        sView.innerText = score.score;
                        storageService.storePlayer(ip, score);
                    }

                } else {
                    storageService.storePlayer(ip, score);
                    if (!playerViewExist(score.playerName)) {
                        createPlayerView(PlayerInfo.fromPlayerScoreMsg(score));
                        nbPlayers = nbPlayers + 1;
                    }
                }
            },
            start => {
                if (start) {
                    buzzButtonContainer.style.display = "block";
                }
            },
            question => {

            }
        );
        eventSources.init();

        /* Logique */
        init(ip);
    }
)




/* Functions */
function init(ip){
    if (authService.isConnected(ip)) {
        registrationBloc.style.display = "none";
        let player = storageService.getPlayer(ip);
        if (!playerViewExist(player.name)) {
            createPlayerView(player);
        }
    } else {
        let player = storageService.getPlayer(ip);
        if (player !== null && playerViewExist(player.name)) {
            let pView = document.getElementById(player.name);
            playersBloc.removeChild(pView);
        }
        playerNameInput.addEventListener("input", ev => {
            name = playerNameInput.value;

        });
        playerNameBtn.onclick = () => {
            if (name !== "") {
                authService.login(name);
            } else {
                alert("Veuillez entrer un pseudo!!!")
            }
        }
    }
}

function createPlayerView(info) {
    let pDiv = document.createElement("div");
    pDiv.id = info.name;
    pDiv.className = "col-2 card";
    pDiv.style = "margin-right: 15px"
    pDiv.innerHTML =
        "<img src='./asset/profil.png' /> " +
        "<h5 class='card-title text-center'> " +
        info.name +
        "</h5>" +
        "<div class='card-body text-center'>" +
        "<h1 class='rounded-pill bg-primary' id='score_" + info.name +
        "'> " + info.score + "</h1>" +
        "</div>"

    playersBloc.append(pDiv);
}

function playerViewExist(player) {
    return document.getElementById(player) !== null;
}