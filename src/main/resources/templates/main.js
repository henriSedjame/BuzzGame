import {AuthService} from "./authService.js";
import {GameService, Score} from "./gameService.js";
import {EventSources} from "./event-sources.js";
import {StorageService} from "./storageService.js";
import {PlayerInfo} from "./player-info.js";
import {GeoService} from "./geoService.js";
import {HttpClient} from "./http-client.js";
import {PlayerScoreMsg} from "./messages.js";


/* Variables globales */
let name = "";
let nbPlayers = 0;

const colorRed = "#FF0000FF";
const colorGrey = "#797878";
const colorBlue = "#0d6efc";
const colorYellow = "#f6b900";
const colorWhite = "#ffffff";

/* Html Elements */



let listPlayersBloc = document.getElementById("listPlayersBloc");
let listPlayers = document.getElementById("listPlayers");
let nbReqPl = document.getElementById("nbReqPl");

let finalBloc = document.getElementById("finalBloc");
let winner = document.getElementById("winner");
let verb = document.getElementById("verb");


let playersBloc = document.getElementById("players");
let buzzButton = document.getElementById("buzzButton");
let buzzButtonContainer = document.getElementById("buzzButtonContainer");

let questionBloc = document.getElementById("questionBloc");
let questionLabel = document.getElementById("questionLabel");
let pointQuestion = document.getElementById("pointQuestion");
let answer1 = document.getElementById("answer_1");
let answer2 = document.getElementById("answer_2");
let answer3 = document.getElementById("answer_3");


let resultBloc = document.getElementById("resultBloc");
let resultBlocFail = document.getElementById("resultBlocFail");
let goodAnswer = document.getElementById("goodAnswer");

let buzzer = null;
let qnumber = null;

let playerz = [];

let winners =[];
let winnerScore = 0;

/* Services */

let httpClient = new HttpClient();

let storageService = new StorageService();

let geoService = new GeoService();

let authService = new AuthService(storageService,
    (player) => {
        name = "";
    },
    msg => {
        name = "";

    }
);

let gameService = new GameService(httpClient);

geoService.getIp(
    ip => {

        let eventSources = new EventSources(
            state => {
                
                let score = state.playerScore;

                let players = state.players;

                let requiredNbPlayers = state.requiredNbPlayers;

                if(players.length < requiredNbPlayers) {

                    listPlayersBloc.style.display = "block";
                    nbReqPl.innerText = requiredNbPlayers;

                    for (const player of players) {

                        if (playerz.indexOf(player) === -1){
                            playerz.push(player);
                            let li = document.createElement("li");
                            li.innerText = player;
                            listPlayers.append(li);
                        }
                    }
                }


                let playerName = score.playerName;

                if (score.update) {
                    let player = storageService.getPlayer(ip, playerName);
                    let pScore = player.score;
                    if (pScore !== score.score) {
                        let sView = document.getElementById("score_" + player.name);
                        sView.innerText = score.score;
                    }
                }


                let plScore = score.score;
                let pn = score.playerName;

                if(plScore === winnerScore){
                    if(winners.indexOf(pn) === -1){
                        winners.push(pn);
                    }
                } else if(score.score > winnerScore){
                    winnerScore = plScore;
                    winners = [];
                    winners.push(pn);
                }

            },
            (start, players) => {


                if (start) {

                    for (const player of players) {
                        createPlayerView(new PlayerInfo(player, 0));
                        storageService.storePlayer(ip, new PlayerScoreMsg(player));
                    }
                    setTimeout(() => {
                        buzzButtonContainer.style.display = "block";
                        listPlayersBloc.style.display = "none";
                    }, 1000)

                } else {
                    buzzButtonContainer.style.display = "none";
                    resultBloc.style.display = "none";
                    resultBlocFail.style.display = "none";
                    finalBloc.style.display = "block";
                    if(winners.length > 1) {
                        verb.innerText = "Et les gagants sont ";
                    } else {
                        verb.innerText = "Et le gagnant est ";
                    }

                    winner.innerText = winners.join(" & ");

                    setTimeout(() => {
                        finalBloc.style.display = "none";
                    }, 2000)
                }
            },
            question => {
                resultBloc.style.display = "none";
                resultBlocFail.style.display = "none";

                qnumber = question.number;

                questionBloc.style.display = "block";
                questionLabel.innerText = question.label;
                pointQuestion.innerText = question.points
                answer1.innerText = question.answers[0].label;
                answer2.innerText = question.answers[1].label;
                answer3.innerText = question.answers[2].label;

                changeBtnColor(answer1, colorBlue);
                changeBtnColor(answer2, colorBlue);
                changeBtnColor(answer3, colorBlue);

                answerClick(ip, answer1, 0, answer2, answer3);
                answerClick(ip, answer2, 1, answer2, answer1);
                answerClick(ip, answer3, 2, answer1, answer2);

            },
            canBuzz => {
                if (canBuzz) {
                    enableBuzz(ip);
                } else {
                    disableBuzz();
                }
            },
            buzz => {

                disableBuzz();
                let pName = buzz.author;
                let pView = document.getElementById(pName);
                pView.style.backgroundColor = colorYellow;
            },
            answer => {
                disableBuzz();
                questionBloc.style.display = "none";

                changeBtnColor(answer1, colorGrey);
                changeBtnColor(answer2, colorGrey);
                changeBtnColor(answer3, colorGrey);

                let pName = answer.playerName;
                let pView = document.getElementById(pName);
                pView.style.backgroundColor = colorWhite;
                if (answer.answer.good) {
                    resultBloc.style.display = "block";
                } else {
                    resultBlocFail.style.display = "block";
                    goodAnswer.innerText = answer.answer.label;
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

    if (authService.isConnected(ip, null)) {
        let player = storageService.getPlayer(ip, playerFromUrl());
        if (!playerViewExist(player.name)) {
            createPlayerView(player);
        }
    } else {
        let player = storageService.getPlayer(ip, playerFromUrl());
        if (player !== null && playerViewExist(player.name)) {
            let pView = document.getElementById(player.name);
            playersBloc.removeChild(pView);
        }
    }
}

function createPlayerView(info) {
    let img = Math.floor(Math.random() * 10);

    let pDiv = document.createElement("div");
    pDiv.id = info.name;
    pDiv.className = "col-2 card";
    pDiv.style = "margin: auto"
    pDiv.innerHTML =
        "<img src='./asset/"+ img +".png' /> " +
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

function enableBuzz(ip) {
    let p = playerFromUrl();
    buzzButton.style.backgroundColor = colorRed;
    buzzButton.onclick = () => {
        buzzer = ip + "-" + p;
        disableBuzz();
        changeBtnColor(answer1, colorBlue);
        changeBtnColor(answer2, colorBlue);
        changeBtnColor(answer3, colorBlue);

        let pName = storageService.getPlayer(ip, p).name;
        gameService.buzz(pName);
    }
}

function disableBuzz() {
    buzzButton.style.backgroundColor = colorGrey;
    buzzButton.onclick = () => {};
}

function answerClick(ip, btn, number, btn2, btn3) {
    let p = playerFromUrl();
    btn.style.backgroundColor = colorGrey;
    btn.onclick = () => {
        if (buzzer === (ip + "-" + p)) {
            let pname = storageService.getPlayer(ip, p).name;
            gameService.answer(pname, qnumber, number);
            changeBtnColor(btn2, colorGrey);
            changeBtnColor(btn3, colorGrey);
            buzzer = null;
        }
    }
}

function playerFromUrl() {
    let parts = window.location.href.split("player=");
    if (parts.length > 1) return parts[1];
    return null;
}