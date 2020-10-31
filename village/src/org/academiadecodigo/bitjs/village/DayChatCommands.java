package org.academiadecodigo.bitjs.village;

public enum DayChatCommands {

    VOTE("/vote"),
    WHISPER("/whisper"),
    LIST("/list");

    private String commandMessage;

    private DayChatCommands(String commandMessage) {

        this.commandMessage=commandMessage;

    }

    public String getCommandMessage() {
        return commandMessage;
    }
}
