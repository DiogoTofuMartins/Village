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
        prompt.sendUserMsg(StringHelper.DAY);
        String actualMessage;
        boolean voted = false;
        while (!userDispatcher.isDead()) {

            System.out.println("getting input");
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
                        GameServer.instanceOf().listUsers();
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

                        runVotingLogic(GameServer.instanceOf(), userDispatcher.toString(), prompt);
                        return;

                    default:
                        System.out.println("this is super weird");
                }
                continue;
            }
            System.out.println("doing your broadcast");
            GameServer.instanceOf().broadcast(actualMessage, userDispatcher);


        }

    }


    public void runVotingLogic(GameServer gameServer, String username, Prompt prompt) {

        MenuInputScanner voteMenu = new MenuInputScanner(gameServer.listUsers());
        voteMenu.setMessage(StringHelper.VOTING);
        int voteIndex = prompt.getUserInput(voteMenu) - 1;
        String votedPlayer = gameServer.listUsers()[voteIndex];
        GameServer.instanceOf().setVotesCounter();
        gameServer.checkPolls(votedPlayer, username);

    }

}
