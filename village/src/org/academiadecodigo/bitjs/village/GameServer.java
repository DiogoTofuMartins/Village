package org.academiadecodigo.bitjs.village;

import org.academiadecodigo.bitjs.village.characters.Character;
import org.academiadecodigo.bitjs.village.characters.CharacterFactory;
import org.academiadecodigo.bootcamp.Prompt;

import javax.xml.stream.events.Characters;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {

    public final int PORT = 7057;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private List<Character> charactersList;

    private ExecutorService executorService;
    private Prompt prompt;
    private boolean characterAttributed;
    private List<Dispatcher> dispatchersList;
    private int counter;
    private boolean full;
    private String playerToSave;
    private String playerToKill;
    private int totalVotes;
    private static GameServer gameServer;


    private GameServer() {
        try {
            this.serverSocket = new ServerSocket(PORT);
            characterAttributed = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.executorService = Executors.newFixedThreadPool(10);

        dispatchersList = Collections.synchronizedList(new LinkedList<>());

    }

    public static GameServer instanceOf(){
        if(gameServer == null){
            gameServer = new GameServer();
            return gameServer;
        }
        return gameServer;
    }

    public static void main(String[] args) {


        while (GameServer.instanceOf().serverSocket.isBound()) {

            GameServer.instanceOf().init();
        }
    }

    public void init() {


        try {

            System.out.println("Waiting connection...");
            clientSocket = serverSocket.accept();
            System.out.println("Accepted");

            prompt = new Prompt(clientSocket.getInputStream(), new PrintStream(clientSocket.getOutputStream()));
            if (full) {
                prompt.sendUserMsg("game full, try again later!");
                return;
            }
            counter++;


            Dispatcher dispatcher = new Dispatcher(clientSocket);
            dispatchersList.add(dispatcher);
            executorService.submit(dispatcher);
            System.out.println(dispatchersList.size());
            prompt.sendUserMsg("waiting for players, please stand by...");

            if (counter == 5) {
                charactersList = CharacterFactory.getList(counter);
                full = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public synchronized void broadcast(String string, Dispatcher dispatcher) {

        for (Dispatcher player : dispatchersList) {
            System.out.println(dispatchersList.size());
            if (player.toString().equals(dispatcher.toString())) {
                continue;
            }
            player.sendUser(string);
        }
        notifyAll();
    }
    public synchronized void broadcast(String string) {

        for (Dispatcher player : dispatchersList) {

            player.sendUser(string);
        }
        notifyAll();
    }


    public synchronized String[] listUsers() {

        String[] playersNames = new String[dispatchersList.size()];

        for (int i = 0; i < dispatchersList.size(); i++) {

            playersNames[i] = dispatchersList.get(i).toString();

        }
        notifyAll();
        return playersNames;
    }


    public synchronized void whisper(String username, String itself, String string) {

        for (Dispatcher player : dispatchersList) {
            if (player.toString().equals(username)) {
                player.sendUser(itself + ": " + string);
            }
        }
        notifyAll();
    }

    public synchronized boolean checkUsername(String userName) {
        boolean isValidUsername = true;
        for (int i = 0; i < dispatchersList.size(); i++) {
            if (userName.equals(dispatchersList.get(i).toString())) {
                isValidUsername = false;
                break;
            }
        }
        notifyAll();
        return isValidUsername;
    }


    public synchronized void attributeMyCharacter(Dispatcher dispatcher) {


        int random = (int) (Math.random() * charactersList.size());
        Character characterToReturn = charactersList.get(random);
        charactersList.remove(random);
        //characterAttributed = true;
        dispatcher.setCharacter(characterToReturn);
        notifyAll();

    }

    public synchronized void savePlayer(String player) {

        for (Dispatcher username : dispatchersList) {

            if (player.equals(username.toString())) {
                this.playerToSave = player;
            }

        }
        notifyAll();

    }

    public synchronized void tryToKillPlayer(String player) {

        for (Dispatcher username : dispatchersList) {

            if (player.equals(username.toString())) {
                this.playerToKill = player;
            }

        }
        notifyAll();

    }

    public synchronized boolean checkKill() {

        return !playerToKill.equals(playerToSave);


    }

    public synchronized void checkPolls(String vote, String voter) {
        broadcast(voter + "voted in " + vote);
        totalVotes++;
        for (Dispatcher dispatcher : dispatchersList) {
            if (vote.equals(dispatcher.toString())) {
                dispatcher.setVotes(dispatcher.getVotes() + 1);
            }
        }

        if (totalVotes == dispatchersList.size()) {

            for (Dispatcher dispatcher : dispatchersList) {
                if (dispatcher.getVotes() >= (dispatchersList.size() / 2)) {
                    dispatcher.setDead();
                    dispatchersList.remove(dispatcher);

                    broadcast(dispatcher.toString() + "was lynched. His role was " +
                             dispatcher.getCharacter().toString());
                    break;
                }

            }
            for (Dispatcher dispatcher : dispatchersList) {
                dispatcher.setVotes(0);
            }
            notifyAll();
        }

        }

        public void addSleeper(){

    }


    public int getCounter() {
        return counter;
    }
}


