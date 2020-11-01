package org.academiadecodigo.bitjs.village;

import org.academiadecodigo.bitjs.village.characters.Character;
import org.academiadecodigo.bitjs.village.utili.StringHelper;
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

            String triedUserName;

            if (GameServer.instanceOf().checkUsername(triedUserName = prompt.getUserInput(usernameQuestion))) {
                userName = triedUserName;
                prompt.sendUserMsg("i Like that Username , it is Valid \n" + "Go ahead and exchange msg with other users");
                GameServer.instanceOf().addUsernamesAttributed();
                break;
            }
            prompt.sendUserMsg("i don't like that Username , it is already in use pick another");
        }
        while (!GameServer.instanceOf().allHaveUsername()){

            addDelay(100);
        }
        enterLobbyChat();
        System.out.println("arrived waiting");
        while (!GameServer.instanceOf().isAllCharacterAttributed()) {

            addDelay(100);
        }

        System.out.println(Thread.currentThread().getName());

        while (!isDead()) {
            prompt.sendUserMsg(StringHelper.NIGHT);
            character.runNightLogic(prompt, this);
            GameServer.instanceOf().addPlayersWhoLeftNight();
            while(!GameServer.instanceOf().didAllLeftNightLogic()){
                GameServer.instanceOf().allLeftNightLogic();
                addDelay(100);
            }
            addDelay(1000);
            if(GameServer.instanceOf().checkEndGame(this)){
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            GameServer.instanceOf().resetVotingEnded();
            if (isDead()) {
                break;
            }
            prompt.sendUserMsg(StringHelper.SUNSET);
            character.runDayLogic(prompt, this);

            GameServer.instanceOf().resetAllLeftNight();
            while (!GameServer.instanceOf().isVotingEnded()){
                addDelay(100);
            }

            if(GameServer.instanceOf().checkEndGame(this)){
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //TODO Use StringHelper in all hardcoded strings

    public void enterLobbyChat() {

        System.out.println("entered lobby chat");
        prompt.sendUserMsg(StringHelper.LOGO);
        prompt.sendUserMsg(StringHelper.INSTRUCTIONS);
        String actualMessage=null;

        boolean isCommand = false;
        while (true) {


            actualMessage = prompt.getUserInput();
            if (actualMessage.equals("")||actualMessage.equals(null)){
                continue;
            }
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
                        prompt.sendUserMsg(StringHelper.WHISPER);
                        String user = prompt.getUserInput();
                        if (!GameServer.instanceOf().checkUsername(user)) {
                            prompt.sendUserMsg(StringHelper.MESSAGE);
                            String secretMessage = prompt.getUserInput();
                            GameServer.instanceOf().whisper(user, userName, secretMessage);
                            notifyAll();
                            break;

                        }
                        sendUser(StringHelper.USER);
                        break;

                    case "/play":
                        System.out.println(GameServer.instanceOf().getStartCounter());
                        if (GameServer.instanceOf().getStartCounter() == 4) {
                            GameServer.instanceOf().broadcast(userName + " is ready to play!", this);
                            GameServer.instanceOf().attributeMyCharacter(this);

                            isCommand=false;
                            return;
                        }
                        break;

                    default:
                        System.out.println("this is super weird");
                        break;
                }
                isCommand= false;
                continue;
            }


            GameServer.instanceOf().broadcast(actualMessage, this);


        }
    }


    public void addDelay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void sendUser(String message) {
        prompt.sendUserMsg(message);
    }

    public void setCharacter(Character character) {
        this.character = character;
        prompt.sendUserMsg(character.toString());

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

