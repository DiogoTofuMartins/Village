package org.academiadecodigo.bitjs.village.characters;

import org.academiadecodigo.bitjs.village.DayChatCommands;
import org.academiadecodigo.bitjs.village.Dispatcher;
import org.academiadecodigo.bitjs.village.GameServer;
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
        prompt.sendUserMsg("THE SUN HAS RISEN.");

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
                        String list = "";
                        for(String name : GameServer.instanceOf().listUsers()){
                            list += name + "\n";
                        }
                        prompt.sendUserMsg(list);
                        break;


                    case "/whisper":

                        prompt.sendUserMsg("who do you want to send the secret message");
                        String user = prompt.getUserInput();
                        if (!GameServer.instanceOf().checkUsername(user)) {
                            prompt.sendUserMsg("What is the message ?");
                            String secretMessage = prompt.getUserInput();
                            GameServer.instanceOf().whisper(user, userDispatcher.toString(), secretMessage);
                            break;

                        }
                        prompt.sendUserMsg("that user does not exist");
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
                        System.out.println("this is super weird");
                }
                continue;
            }
            System.out.println("doing your broadcast");
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

        voteMenu.setMessage("who do you want to linch ?");
        int voteIndex = prompt.getUserInput(voteMenu) - 1;
        String votedPlayer = gameServer.listUsers()[voteIndex];
        GameServer.instanceOf().addTotalVotes();
        gameServer.checkPolls(votedPlayer, username);

    }

}
