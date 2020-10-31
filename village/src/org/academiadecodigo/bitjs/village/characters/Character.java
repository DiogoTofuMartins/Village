package org.academiadecodigo.bitjs.village.characters;

import org.academiadecodigo.bitjs.village.CommandsLobby;
import org.academiadecodigo.bitjs.village.DayChatCommands;
import org.academiadecodigo.bitjs.village.Dispatcher;
import org.academiadecodigo.bitjs.village.GameServer;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

public class Character {

    public void runNightLogic(Prompt prompt, GameServer gameServer){

    }

    public void runDayLogic(GameServer gameServer, Prompt prompt, Dispatcher userDispatcher){
        String actualMessage;
        boolean voted= false;
        while (true) {
            synchronized (this) {

                actualMessage = prompt.getUserInput();
                boolean isCommand = false;
                for (int i = 0; i < DayChatCommands.values().length; i++) {

                    if (actualMessage.equals(DayChatCommands.values()[i].getCommandMessage())) {
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
                            if (!gameServer.checkUsername(user)){
                                prompt.sendUserMsg("What is the message ?");
                                String secretMessage = prompt.getUserInput();
                                gameServer.whisper(user , userDispatcher.toString() , secretMessage);
                                break;

                            }
                            prompt.sendUserMsg("that user does not exist");
                            break;

                        case "/vote":
                            if (!voted) {
                                voted = true;
                                runVotingLogic(gameServer, userDispatcher.toString(), prompt);
                            }
                            break;

                        case "/sleep":
                            if (voted){
                                gameServer.addSleeper();
                                return;
                            }
                            prompt.sendUserMsg("You must vote before you sleep");
                            break;
                        default:
                            System.out.println("this is super weird");
                    }
                    continue;
                }

                gameServer.broadcast(actualMessage, userDispatcher);


            }
        }

    }

    public void runVotingLogic(GameServer gameServer,String username,Prompt prompt){

        MenuInputScanner voteMenu = new MenuInputScanner(gameServer.listUsers(username));
        voteMenu.setMessage("who do you want to linch ?");
        int voteIndex = prompt.getUserInput(voteMenu)-1;
        String votedPlayer = gameServer.listUsers(username)[voteIndex];
        gameServer.checkPoll(votedPlayer,username);

    }

}
