package org.academiadecodigo.bitjs.village.characters;

import org.academiadecodigo.bootcamp.Prompt;

public abstract class Character {

    private Prompt prompt;

    public Character(Prompt prompt){
        this.prompt = prompt;
    }

    abstract void runNightLogic();

    abstract void runDayLogic();

}
