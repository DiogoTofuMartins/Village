package org.academiadecodigo.bitjs.village.characters;


import org.academiadecodigo.bitjs.village.Dispatcher;
import org.academiadecodigo.bitjs.village.GameServer;
import org.academiadecodigo.bitjs.village.utili.StringHelper;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

public class Werewolf extends Character {

    @Override
    public void runNightLogic(Prompt prompt, Dispatcher dispatcher) {

        String[] names = GameServer.instanceOf().listUsers();
        String[] excludeUser = new String[names.length - 1];
        int j = 0;

        for (int i = 0; i < names.length; i++) {
            if (!names[i].equals(dispatcher.toString())) {
                excludeUser[j] = names[i];
                j++;
            }

        }
        MenuInputScanner menuWerewolf = new MenuInputScanner(excludeUser);
        menuWerewolf.setMessage(StringHelper.KILL);

        int response = prompt.getUserInput(menuWerewolf);

        String player = excludeUser[response - 1];

        GameServer.instanceOf().tryToKillPlayer(player);
    }

    @Override
    public String toString() {
        return StringHelper.WEREWOLF;
    }
}
