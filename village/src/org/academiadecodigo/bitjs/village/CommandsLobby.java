package org.academiadecodigo.bitjs.village;

public enum CommandsLobby {

    PLAY("/play"),
    WHISPER("/whisper"),
    LIST("/list");

    private String commandMessage;

    private CommandsLobby(String commandMessage) {

        this.commandMessage=commandMessage;

    }

    public String getCommandMessage() {
        return commandMessage;
    }
}
