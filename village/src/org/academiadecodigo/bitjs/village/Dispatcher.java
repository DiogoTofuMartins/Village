package org.academiadecodigo.bitjs.village;

import org.academiadecodigo.bitjs.village.characters.Character;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Dispatcher implements Runnable{

    private Socket clientSocket;
    private Character character;
    private GameServer gameServer;
    private String name;
    private Prompt prompt;

    public Dispatcher(Socket socket, Character character, GameServer gameServer){
        this.clientSocket = socket;
        this.character = character;
        this.gameServer = gameServer;
        try {
            this.prompt = new Prompt(socket.getInputStream(),new PrintStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {

        StringInputScanner usernameQuestion = new StringInputScanner();
        usernameQuestion.setMessage("What is your username?");

        name = prompt.getUserInput(usernameQuestion);
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        character.start();
        character.runNightLogic


    }

    @Override
    public String toString() {
        return name;
    }
}
