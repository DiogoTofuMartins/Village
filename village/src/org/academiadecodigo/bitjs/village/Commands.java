package org.academiadecodigo.bitjs.village;

import java.awt.*;

public enum Commands {

    PLAY("/play"),
    WHISPER("/whisper"),
    LIST("/list");

    private String commandMessage;

    private Commands(String commandMessage) {

        this.commandMessage=commandMessage;

    }

    public String getCommandMessage() {
        return commandMessage;
    }
}
