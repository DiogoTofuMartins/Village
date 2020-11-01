package org.academiadecodigo.bitjs.village.characters;

import org.academiadecodigo.bitjs.village.DayChatCommands;
import org.academiadecodigo.bitjs.village.Dispatcher;
import org.academiadecodigo.bitjs.village.GameServer;
import org.academiadecodigo.bitjs.village.utili.StringHelper;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

public class Character {

    public void runNightLogic(Prompt prompt, Dispatcher dispatcher) {

    }

    public void runDayLogic(Prompt prompt, Dispatcher userDispatcher) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        prompt.sendUserMsg("\033[H\033[2J");
        prompt.sendUserMsg(StringHelper.DAY);
        String actualMessage;
        boolean voted = false;

        while (!userDispatcher.isDead()) {

            actualMessage = prompt.getUserInput();

            if (actualMessage.equals("")||actualMessage.equals(null)){
                continue;
            }

            boolean isCommand = false;
            for (int i = 0; i < DayChatCommands.values().length; i++) {

                if (actualMessage.equals(DayChatCommands.values()[i].getCommandMessage())) {
                    isCommand = true;
                }
            }

            if (isCommand) {


                switch (actualMessage) {

                    case "/list":

                        String list = "";

                        for(String name : GameServer.instanceOf().listUsers()){
                            list += name + "\n";
                        }

                        prompt.sendUserMsg(list);
                        break;

                    case "/whisper":

                        prompt.sendUserMsg(StringHelper.WHISPER);
                        String user = prompt.getUserInput();

                        if (!GameServer.instanceOf().checkUsername(user)) {
                            prompt.sendUserMsg(StringHelper.MESSAGE);
                            String secretMessage = prompt.getUserInput();
                            GameServer.instanceOf().whisper(user, userDispatcher.toString(), secretMessage);
                            break;
                        }

                        prompt.sendUserMsg(StringHelper.USER);
                        break;

                    case "/vote":

                        prompt.sendUserMsg("\033[H\033[2J");
                        GameServer.instanceOf().addVotersReady();
                        GameServer.instanceOf().broadcast(userDispatcher.toString()+ " is ready to vote hurry up");

                        while (!GameServer.instanceOf().isVotingStarted()){
                            GameServer.instanceOf().checkVotingStarted();
                            userDispatcher.addDelay(100);
                        }

                        runVotingLogic(GameServer.instanceOf(), userDispatcher.toString(), prompt);
                        return;

                    default:
                }
                continue;
            }

            GameServer.instanceOf().broadcast(actualMessage, userDispatcher);
        }
    }

    public void runVotingLogic(GameServer gameServer, String username, Prompt prompt) {

        String[] names = GameServer.instanceOf().listUsers();
        String[] excludeUser = new String[names.length - 1];
        int j = 0;

        for (int i = 0; i < names.length; i++) {
            if (!names[i].equals(username)) {
                excludeUser[j] = names[i];
                j++;
            }
        }

        MenuInputScanner voteMenu = new MenuInputScanner(excludeUser);

        voteMenu.setMessage(StringHelper.VOTING);

        int voteIndex = prompt.getUserInput(voteMenu) - 1;
        String votedPlayer = excludeUser[voteIndex];
        GameServer.instanceOf().addTotalVotes();
        gameServer.checkPolls(votedPlayer, username);
    }
}
