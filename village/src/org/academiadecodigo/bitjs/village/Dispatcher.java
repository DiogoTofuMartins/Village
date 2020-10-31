package org.academiadecodigo.bitjs.village;

import org.academiadecodigo.bitjs.village.characters.Character;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Dispatcher implements Runnable {

    private Socket clientSocket;
    private Character character;
    private GameServer gameServer;
    private String userName;
    private Prompt prompt;
    private int votes;
    private boolean dead;


    public Dispatcher(Socket socket, GameServer gameServer) {
        this.clientSocket = socket;

        this.gameServer = gameServer;
        try {
            this.prompt = new Prompt(socket.getInputStream(), new PrintStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());

        StringInputScanner usernameQuestion = new StringInputScanner();
        usernameQuestion.setMessage("What is your username?");


        while (userName == null) {
            //System.out.println("enter while");
            String triedUserName;

            if (gameServer.checkUsername(triedUserName = prompt.getUserInput(usernameQuestion))) {
                System.out.println("ekefkef");
                userName = triedUserName;
                prompt.sendUserMsg("i Like that Username , it is Valid \n" + "Go ahead and exchange msg with other users");

                break;
            }
            prompt.sendUserMsg("i don't like that Username , it is already in use pick another");
        }
        System.out.println(Thread.currentThread().getName());
        enterLobbyChat();

    /*
            synchronized (gameServer) {

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }*/
        System.out.println(Thread.currentThread().getName());
        while (!isDead()) {
            System.out.println(Thread.currentThread().getName() + "is not dead");
            character.runNightLogic(prompt, gameServer, this);
           /* synchronized (gameServer) {
               try {
                    wait();
*/
            if (isDead()) {
                break;
            }
            character.runDayLogic(prompt, gameServer, this);
                 /*  synchronized (gameServer) {
                        wait();
                    }
                } catch(InterruptedException e){
                        e.printStackTrace();
                    }*/
        }
    }


    //TODO Use StringHelper in all hardcoded strings

    public void enterLobbyChat() {
        String actualMessage;
        while (true) {


            actualMessage = prompt.getUserInput();
            boolean isCommand = false;
            for (int i = 0; i < CommandsLobby.values().length; i++) {

                if (actualMessage.equals(CommandsLobby.values()[i].getCommandMessage())) {
                    isCommand = true;
                }
            }
            if (isCommand) {


                switch (actualMessage) {


                    case "/list":
                        gameServer.listUsers();
                        break;


                    case "/whisper":
                        prompt.sendUserMsg("who do you want to send the secret message");
                        String user = prompt.getUserInput();
                        if (!gameServer.checkUsername(user)) {
                            prompt.sendUserMsg("What is the message ?");
                            String secretMessage = prompt.getUserInput();
                            gameServer.whisper(user, userName, secretMessage);
                            break;

                        }
                        sendUser("that user does not exist");
                        break;

                    case "/play":
                        gameServer.broadcast(userName + " is ready to play", this);
                        gameServer.attributeMyCharacter(this);
                        System.out.println(character.toString());
                        return;

                    default:
                        System.out.println("this is super weird");
                }
                continue;
            }

            gameServer.broadcast(actualMessage, this);


        }
    }


    public void sendUser(String message) {
        prompt.sendUserMsg(message);
    }

    public void setCharacter(Character character) {
        this.character = character;
        prompt.sendUserMsg("you are " + character.toString());

    }


    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getVotes() {
        return votes;
    }

    @Override
    public String toString() {
        return userName;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead() {
        this.dead = true;
    }

    public Character getCharacter() {
        return character;
    }
}

