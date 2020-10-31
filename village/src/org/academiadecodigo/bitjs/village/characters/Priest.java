package org.academiadecodigo.bitjs.village.characters;

import org.academiadecodigo.bitjs.village.Dispatcher;
import org.academiadecodigo.bitjs.village.GameServer;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

public class Priest extends Character{


    @Override
    public void runNightLogic(Prompt prompt, GameServer gameServer, Dispatcher dispatcher) {

        MenuInputScanner menuPriest = new MenuInputScanner(gameServer.listUsers());
        menuPriest.setMessage("Who do you want to save?");

        int response = prompt.getUserInput(menuPriest);

        String player = gameServer.listUsers()[response - 1];

        gameServer.savePlayer(player);

    }


}
