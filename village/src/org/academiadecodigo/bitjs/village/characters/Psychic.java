package org.academiadecodigo.bitjs.village.characters;


import org.academiadecodigo.bitjs.village.Dispatcher;

import org.academiadecodigo.bitjs.village.GameServer;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

public class Psychic extends Character{

    @Override
    public void runNightLogic(Prompt prompt, GameServer gameServer, Dispatcher dispatcher) {

        MenuInputScanner menuPsychic = new MenuInputScanner(gameServer.listUsers(dispatcher.toString()));
        menuPsychic.setMessage("Do you have an esoteric perception of anyone?");

        int response = prompt.getUserInput(menuPsychic);


        String player = gameServer.listUsers(dispatcher.toString())[response - 1];

        gameServer.tryToKillPlayer(player);


    }



}
