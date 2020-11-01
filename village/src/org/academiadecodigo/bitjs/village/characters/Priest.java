package org.academiadecodigo.bitjs.village.characters;
import org.academiadecodigo.bitjs.village.Dispatcher;
import org.academiadecodigo.bitjs.village.GameServer;
import org.academiadecodigo.bitjs.village.utili.StringHelper;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

public class Priest extends Character{


    @Override
    public void runNightLogic(Prompt prompt, Dispatcher dispatcher) {

        MenuInputScanner menuPriest = new MenuInputScanner(GameServer.instanceOf().listUsers());
        menuPriest.setMessage(StringHelper.PRIESTPHRASE);

        int response = prompt.getUserInput(menuPriest);

        String player = GameServer.instanceOf().listUsers()[response - 1];

        GameServer.instanceOf().savePlayer(player);

    }

    @Override
    public String toString() {
        return StringHelper.PRIEST;
    }
}
