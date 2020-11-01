package org.academiadecodigo.bitjs.village;

import org.academiadecodigo.bitjs.village.characters.Character;
import org.academiadecodigo.bitjs.village.characters.CharacterFactory;
import org.academiadecodigo.bitjs.village.characters.Werewolf;
import org.academiadecodigo.bitjs.village.utili.StringHelper;
import org.academiadecodigo.bootcamp.Prompt;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {

    private static GameServer gameServer;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private List<Character> charactersList;
    private ExecutorService executorService;
    private Prompt prompt;
    private List<Dispatcher> dispatchersList;
    public final int PORT = 7057;
    public final int N_PLAYERS = 5;
    private int startCounter;
    private int votersReady;
    private int charactersAttributed;
    private int totalVotes;
    private int usernamesAttributed;
    private int playersWhoLeftNight;
    private boolean allCharacterAttributed;
    private boolean full;
    private boolean votingStarted = false;
    private boolean votingEnded = false;
    private boolean allLeftNight = false;
    private String playerToSave;
    private String playerToKill;

    private GameServer() {

        try {

            this.serverSocket = new ServerSocket(PORT);
            allCharacterAttributed = false;
            charactersAttributed = 0;

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.executorService = Executors.newFixedThreadPool(10);
        this.dispatchersList = Collections.synchronizedList(new LinkedList<>());
    }

    public static GameServer instanceOf() {

        if (gameServer == null) {
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
                prompt.sendUserMsg(StringHelper.GAMEFULL);
                clientSocket.close();
                return;
            }

            startCounter++;
            Dispatcher dispatcher = new Dispatcher(clientSocket);
            dispatchersList.add(dispatcher);
            executorService.submit(dispatcher);
            prompt.sendUserMsg(StringHelper.WAITING);

            if (startCounter == N_PLAYERS) {
                charactersList = CharacterFactory.getList(startCounter);
                full = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized void broadcast(String string, Dispatcher dispatcher) {

        for (Dispatcher player : dispatchersList) {

            if (player.toString().equals(dispatcher.toString())) {
                continue;
            }

            player.sendUser(dispatcher.toString() + ": " + string);
        }
    }

    public synchronized void broadcast(String string) {

        for (Dispatcher player : dispatchersList) {

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


    public synchronized void whisper(String username, String itself, String string) {

        for (Dispatcher player : dispatchersList) {
            if (player.toString().equals(username)) {
                player.sendUser(itself + ": " + string);
            }
        }
    }

    public synchronized boolean checkUsername(String userName) {

        boolean isValidUsername = true;

        for (int i = 0; i < dispatchersList.size(); i++) {

            if (userName.equals(dispatchersList.get(i).toString())) {
                isValidUsername = false;
                break;
            }
        }

        return isValidUsername;
    }

    public synchronized boolean allHaveUsername() {
        return usernamesAttributed == N_PLAYERS;

    }

    public synchronized void attributeMyCharacter(Dispatcher dispatcher) {

        int random = (int) (Math.random() * charactersList.size());
        Character characterToReturn = charactersList.get(random);
        charactersList.remove(random);
        charactersAttributed++;
        dispatcher.setCharacter(characterToReturn);

        if (charactersAttributed == N_PLAYERS) {
            allCharacterAttributed = true;
        }

    }

    public boolean allLeftNightLogic() {

        if (playersWhoLeftNight == dispatchersList.size()) {
            allLeftNight = true;
            checkKill();
            playersWhoLeftNight = 0;
            return true;
        }

        return playersWhoLeftNight == dispatchersList.size();

    }

    public synchronized void savePlayer(String player) {

        for (Dispatcher username : dispatchersList) {

            if (player.equals(username.toString())) {
                this.playerToSave = player;
            }
        }

    }

    public synchronized boolean guessPlayer(String player) {

        for (Dispatcher username : dispatchersList) {

            if (player.equals(username.toString()) && username.getCharacter() instanceof Werewolf) {
                return true;
            }
        }

        return false;
    }

    public synchronized void tryToKillPlayer(String player) {

        for (Dispatcher username : dispatchersList) {

            if (player.equals(username.toString())) {
                this.playerToKill = player;
                System.out.println(playerToKill);
            }
        }
    }

    public synchronized void checkKill() {

        if (!playerToKill.equals(playerToSave)) {

            for (Dispatcher dispatcher : dispatchersList) {

                if (dispatcher.toString().equals(playerToKill)) {
                    dispatcher.setDead();
                    dispatcher.sendUser(StringHelper.DEAD);
                    dispatchersList.remove(dispatcher);
                    broadcast(dispatcher.toString() + " was killed by the wolf \n"
                            + dispatcher.toString() + "'s role was a " + dispatcher.getCharacter().getClass().getSimpleName());
                    return;
                }
            }
        }

        broadcast(StringHelper.GOODDAY);

    }

    public synchronized boolean checkEndGame(Dispatcher player) {

        boolean answer = true;

        for (Dispatcher dispatcher : dispatchersList) {

            if (dispatcher.getCharacter() instanceof Werewolf) {

                if (dispatchersList.size() <= 2) {
                    player.sendUser(StringHelper.WOLFWON);

                    return answer;
                }
                return false;
            }
        }

        player.sendUser(StringHelper.VILLAGEWON);
        return answer;
    }

    public synchronized void checkPolls(String vote, String voter) {

        broadcast(voter + " voted in " + vote);

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
                    dispatcher.sendUser(StringHelper.DEAD);
                    broadcast(dispatcher.toString() + " was lynched. His role was a " +
                            dispatcher.getCharacter().getClass().getSimpleName());
                    votingEnded = true;
                    resetTotalVotes();
                    break;
                }
            }

            if (!votingEnded) {
                broadcast(StringHelper.VOTINGINC);
                votingEnded = true;
                resetTotalVotes();
            }

            for (Dispatcher dispatcher : dispatchersList) {
                dispatcher.setVotes(0);
            }

        }
    }

    public int getStartCounter() {
        return startCounter;
    }


    public void addTotalVotes() {
        this.totalVotes++;
    }

    public void addUsernamesAttributed() {
        this.usernamesAttributed++;
    }

    public void addPlayersWhoLeftNight() {
        this.playersWhoLeftNight++;
    }

    public boolean isAllCharacterAttributed() {
        return allCharacterAttributed;
    }

    public boolean didAllLeftNightLogic() {
        return allLeftNight;
    }

    public void resetAllLeftNight() {
        this.allLeftNight = false;
    }

    public boolean isVotingEnded() {
        return votingEnded;
    }

    public void resetVotingEnded() {
        votingEnded = false;
    }

    public void addVotersReady() {
        this.votersReady++;
    }

    public void resetTotalVotes() {
        this.totalVotes = 0;
    }

    public void resetVotingStarted() {
        this.votingStarted = false;
    }

    public boolean isVotingStarted() {
        return votingStarted;
    }

    public void checkVotingStarted() {
        if (votersReady == dispatchersList.size()) {
            votersReady = 0;
            votingStarted = true;
        }
    }
}


