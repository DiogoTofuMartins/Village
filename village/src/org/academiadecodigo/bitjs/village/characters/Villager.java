package org.academiadecodigo.bitjs.village.characters;
import org.academiadecodigo.bitjs.village.Dispatcher;
import org.academiadecodigo.bitjs.village.GameServer;
import org.academiadecodigo.bootcamp.Prompt;

public class Villager extends Character{


    @Override
    public void runNightLogic(Prompt prompt, Dispatcher dispatcher) {

        prompt.sendUserMsg("What a beautiful night. I should get some sleep.");


    }


}
