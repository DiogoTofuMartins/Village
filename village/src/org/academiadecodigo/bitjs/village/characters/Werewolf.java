package org.academiadecodigo.bitjs.village.characters;

public class Werewolf extends Character {


    @Override
    void runNightLogic() {

    }

    @Override
    void runDayLogic() {
        gameServer.broadcast();
    }
}
