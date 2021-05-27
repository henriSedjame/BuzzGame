import {AuthService} from "./authService.js";
import {GameService, Score} from "./gameService.js";
import {EventSources} from "./event-sources.js";
import {StorageService} from "./storageService.js";
import {PlayerInfo} from "./player-info.js";
import {GeoService} from "./geoService.js";
import {HttpClient} from "./http-client.js";


/* Variables globales */
let name = "";
let nbPlayers = 0;

const colorRed = "#FF0000FF";
const colorGrey = "#4e4c4c";
const colorBlue = "#0d6efc";
const colorYellow = "#f6b900";
const colorWhite = "#ffffff";

/* Html Elements */
let registrationBloc = document.getElementById("registrationBloc");
let playerNameInput = document.getElementById("playerNameInput");
let playerNameBtn = document.getElementById("playerNameBtn");
let playersBloc = document.getElementById("players");
let buzzButton = document.getElementById("buzzButton");
let buzzButtonContainer = document.getElementById("buzzButtonContainer");

let questionBloc = document.getElementById("questionBloc");
let questionLabel = document.getElementById("questionLabel");
let answer1 = document.getElementById("answer_1");
let answer2 = document.getElementById("answer_1");
let answer3 = document.getElementById("answer_1");

let buzzer = null;
let qnumber = null;


/* Services */

let httpClient = new HttpClient();

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

let gameService = new GameService(httpClient);

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
                qnumber = question.number;

                questionBloc.style.display = "none";
                questionLabel.innerText = question.label;
                answer1.innerText = question.answers[0].label;
                answer2.innerText = question.answers[1].label;
                answer3.innerText = question.answers[2].label;

                changeBtnColor(answer1, colorBlue);
                changeBtnColor(answer2, colorBlue);
                changeBtnColor(answer3, colorBlue);

                answerClick(answer1, 0, answer2, answer3);
                answerClick(answer2, 1, answer2, answer1);
                answerClick(answer3, 3, answer1, answer2);

            },
            canBuzz => {
                if(canBuzz) {enableBuzz();  } else { disableBuzz();}
            },
            buzz => {
                let pName = buzz.author;
                let pView = document.getElementById(pName);
                pView.style.backgroundColor = colorYellow;
            },
            answer => {
                let pName = answer.playerName;
                let pView = document.getElementById(pName);
                pView.style.backgroundColor = colorYellow;

                if(answer.answer.good){
                    alert("Bonne réponse !")
                } else {
                    alert("Mauvaise réponse! ")
                }

            }
        );
        eventSources.init();

        /* Logique */
        init(ip);
    }
)


/* Functions */
function init(ip) {
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

function changeBtnColor(btn, color) {
    btn.style.backgroundColor = color;
    btn.style.borderColor = color;
}

function enableBuzz() {
    buzzButton.onclick = () => {
        buzzer = ip;
        disableBuzz();
        let pname = storageService.getPlayer(ip).name;
        gameService.buzz(pname);
    }
}

function disableBuzz() {
    buzzButton.onclick = null;
    buzzButton.style.backgroundColor = colorGrey;
}

function answerClick(btn, number, btn2, btn3) {
    btn.onclick = () => {
        if (buzzer === ip) {
            let pname = storageService.getPlayer(ip).name;
            gameService.answer(pname, qnumber, number);
            changeBtnColor(btn2, colorGrey);
            changeBtnColor(btn3, colorGrey);
            buzzer = null;
        }
    }
}