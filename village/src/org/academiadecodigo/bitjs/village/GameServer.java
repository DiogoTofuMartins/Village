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

    public final int PORT = 7058;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private List<Character> charactersList;

    private ExecutorService executorService;
    private Prompt prompt;
    private boolean characterAttributed;
    private List<Dispatcher> dispatchersList;
    private int counter;
    private boolean full;


    public GameServer() {
        try {
            this.serverSocket = new ServerSocket(PORT);
            characterAttributed = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.executorService = Executors.newCachedThreadPool();

        dispatchersList = Collections.synchronizedList(new LinkedList<>());

    }

    public static void main(String[] args) {
        GameServer chat = new GameServer();

        while (chat.serverSocket.isBound()) {

            chat.init();
        }
    }

    public void init() {


        try {

            System.out.println("Waiting connection...");
            clientSocket = serverSocket.accept();
            System.out.println("Accepted");

            prompt = new Prompt(clientSocket.getInputStream(), new PrintStream(clientSocket.getOutputStream()));
            if(full){
                prompt.sendUserMsg("game full, try again later!");
                return;
            }
            counter++;
            prompt.sendUserMsg("waiting for players, please stand by...");

            Dispatcher dispatcher = new Dispatcher(clientSocket, this);
            executorService.submit(dispatcher);
            dispatchersList.add(dispatcher);
            if(counter == 10){
                charactersList = CharacterFactory.getList(counter);
                full = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public synchronized void broadcast(String string, Dispatcher dispatcher){


        for (Dispatcher player : dispatchersList) {
            if(player.toString().equals(dispatcher.toString())){
                continue;
            }
            player.sendUser(string);
        }
    }

    public synchronized String[] listUsers() {

        String[] playersNames = new String[dispatchersList.size()];

        for (int i = 0; i < dispatchersList.size(); i++) {

            playersNames[i] = dispatchersList.get(i).toString();

        }

        return playersNames;
    }

    public synchronized String[] listUsers(String userName) {

        String[] playersNames = new String[dispatchersList.size()];

        for (int i = 0; i < dispatchersList.size(); i++) {
            if (dispatchersList.get(i).toString().equals(userName)) {
               continue;
            }
            playersNames[i] = dispatchersList.get(i).toString();

        }

        return playersNames;
    }

    public synchronized void whisper(String username, String itself, String string) {

        for (Dispatcher player : dispatchersList) {
            if (player.toString().equals(username)) {
                player.sendUser(itself + ": " + string);
            }
        }
    }

    public synchronized boolean checkUsername(String string){

        for (Dispatcher player : dispatchersList) {
            if (player.toString().equals(string)) {

                return false;

            }
        }
        return true;
    }

    public synchronized void attributeMyCharacter(Dispatcher dispatcher) {


            int random = (int) (Math.random() * charactersList.size());
            Character characterToReturn = charactersList.get(random);
            charactersList.remove(random);
            characterAttributed = true;
            dispatcher.setCharacter(characterToReturn);

        }


}


