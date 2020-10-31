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
    private String userName;
    private Prompt prompt;
    private int votes;


    public Dispatcher(Socket socket, GameServer gameServer){
        this.clientSocket = socket;

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


        while (userName == null) {
            String triedUserName;

            if (gameServer.checkUsername(triedUserName=prompt.getUserInput(usernameQuestion))) {
                userName = triedUserName;
                prompt.sendUserMsg("i Like that Username , it is Valid \n" + "Go ahead and exchange msg with other users");

                break;
            }
            prompt.sendUserMsg("i don't like that Username , it is already in use pick another");
        }

        enterLobbyChat();

        synchronized (gameServer){

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (!isDead){

            character.runNightlogic
        }




    } //TODO Use StringHelper in all hardcoded strings

    public void enterLobbyChat(){
        String actualMessage;
        while (!gameServer.isPlaying) {
            synchronized (this) {

                actualMessage = prompt.getUserInput();
                boolean isCommand = false;
                for (int i = 0; i < Commands.values().length; i++) {

                    if (actualMessage.equals(Commands.values()[i].getCommandMessage())) {
                        isCommand = true;
                    }
                }
                if (isCommand) {


                    switch (actualMessage) {


                        case "/list":
                            gameServer.listUsers();
                            break;


                        case "/whisper":
                            String user = askUser("who do you want to send the secret message");
                            if (server.testValidUser(user)){
                                String secretMessage = askUser("What is the message ?");
                                server.sendParticularUser(secretMessage,user,userName);
                                break;

                            }
                            sendUser("that user does not exist");
                            break;

                        case "/play":
                            gameServer.broadcast(userName + " is ready to play");
                            gameServer.atributeMyCharacter();
                            return;

                        default:
                            System.out.println("this is super weird");
                    }
                    continue;
                }

                server.broadcast(actualMessage, this);


            }
        }




    }

    public void sendUser(String message){
        prompt.sendUserMsg(message);
    }

    public void setCharacter(Character character) {
        this.character = character;
        prompt.sendUserMsg("you are "+ character.toString());

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




}

