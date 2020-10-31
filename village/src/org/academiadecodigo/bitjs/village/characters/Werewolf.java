package org.academiadecodigo.bitjs.village.characters;

import org.academiadecodigo.bitjs.village.Dispatcher;
import org.academiadecodigo.bitjs.village.GameServer;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

public class Werewolf extends Character {

    @Override
    public void runNightLogic(Prompt prompt, GameServer gameServer, Dispatcher dispatcher) {

        MenuInputScanner menuWerewolf = new MenuInputScanner(gameServer.listUsers(dispatcher.toString()));
        menuWerewolf.setMessage("Who do you want to kill?");

        int response = prompt.getUserInput(menuWerewolf);

        String player = gameServer.listUsers(dispatcher.toString())[response - 1];

        gameServer.tryToKillPlayer(player);
    }


}
