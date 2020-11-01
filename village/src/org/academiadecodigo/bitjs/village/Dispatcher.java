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
    private String userName;
    private Prompt prompt;
    private int votes;
    private boolean dead;


    public Dispatcher(Socket socket) {
        this.clientSocket = socket;


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

            if (GameServer.instanceOf().checkUsername(triedUserName = prompt.getUserInput(usernameQuestion))) {
                userName = triedUserName;
                prompt.sendUserMsg("i Like that Username , it is Valid \n" + "Go ahead and exchange msg with other users");

                break;
            }
            prompt.sendUserMsg("i don't like that Username , it is already in use pick another");
        }
        //System.out.println(Thread.currentThread().getName());
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
            character.runNightLogic(prompt, this);
            System.out.println("left night logic");
           /* synchronized (gameServer) {
               try {
                    wait();
*/

            if (isDead()) {
                break;
            }
            System.out.println("arriving day logic");
            character.runDayLogic(prompt, this);
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
        System.out.println("entered lobbychat");
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
                        GameServer.instanceOf().listUsers();
                        notifyAll();
                        break;


                    case "/whisper":
                        prompt.sendUserMsg("who do you want to send the secret message");
                        String user = prompt.getUserInput();
                        if (!GameServer.instanceOf().checkUsername(user)) {
                            prompt.sendUserMsg("What is the message ?");
                            String secretMessage = prompt.getUserInput();
                            GameServer.instanceOf().whisper(user, userName, secretMessage);
                            notifyAll();
                            break;

                        }
                        sendUser("that user does not exist");
                        break;

                    case "/play":
                        if(GameServer.instanceOf().getCounter() == 5) {
                            GameServer.instanceOf().broadcast(userName + " is ready to play", this);
                            GameServer.instanceOf().attributeMyCharacter(this);
                            //character.runNightLogic(prompt, this);
                            return;
                        }
                        break;

                    default:
                        System.out.println("this is super weird");
                        break;
                }
                continue;
            }

            GameServer.instanceOf().broadcast(actualMessage, this);


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

