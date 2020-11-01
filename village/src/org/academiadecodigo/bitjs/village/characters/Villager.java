package org.academiadecodigo.bitjs.village.characters;
import org.academiadecodigo.bitjs.village.Dispatcher;
import org.academiadecodigo.bitjs.village.GameServer;
import org.academiadecodigo.bitjs.village.utili.StringHelper;
import org.academiadecodigo.bootcamp.Prompt;

public class Villager extends Character{


    @Override
    public void runNightLogic(Prompt prompt, Dispatcher dispatcher) {

        prompt.sendUserMsg(StringHelper.VILLAGERPHRASE);
        prompt.sendUserMsg(StringHelper.ENTER);
        prompt.getUserInput();
        return;


    }

    @Override
    public String toString() {
        return StringHelper.VILLAGER;
    }
}
