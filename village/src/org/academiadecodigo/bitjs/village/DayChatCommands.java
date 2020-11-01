package org.academiadecodigo.bitjs.village;

public enum DayChatCommands {

    VOTE("/vote"),
    WHISPER("/whisper"),
    LIST("/list");

    private String commandMessage;

    DayChatCommands(String commandMessage) {

        this.commandMessage = commandMessage;

    }

    public String getCommandMessage() {
        return commandMessage;
    }
    }
