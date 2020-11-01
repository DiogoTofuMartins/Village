package org.academiadecodigo.bitjs.village.utili;

import java.net.PortUnreachableException;

public class StringHelper {

        public static final String LOGO ="            )                (    (     (                           \r\n  *   )  ( /(                )\\ ) )\\ )  )\\ )    (      (            \r\n` )  /(  )\\()) (     (   (  (()/((()/( (()/(    )\\     )\\ )    (    \r\n ( )(_))((_)\\  )\\    )\\  )\\  /(_))/(_)) /(_))((((_)(  (()/(    )\\   \r\n(_(_())  _((_)((_)  ((_)((_)(_)) (_))  (_))   )\\ _ )\\  /(_))_ ((_)  \r\n|_   _| | || || __| \\ \\ / / |_ _|| |   | |    (_)_\\(_)(_)) __|| __| \r\n  | |   | __ || _|   \\ V /   | | | |__ | |__   / _ \\    | (_ || _|  \r\n  |_|   |_||_||___|   \\_/   |___||____||____| /_/ \\_\\    \\___||___| \r\n";
        public static final String SUNSET = "                        |\r\n                    \\       /\r\n                      .-\"-.\r\n                 --  /     \\  --\r\n`~~^~^~^~^~^~^~^~^~^-=======-~^~^~^~~^~^~^~^~^~^~^~`\r\n`~^_~^~^~-~^_~^~^_~-=========- -~^~^~^-~^~^_~^~^~^~`\r\n`~^~-~~^~^~-^~^_~^~~ -=====- ~^~^~-~^~_~^~^~~^~-~^~`\r\n`~^~^~-~^~~^~-~^~~-~^~^~-~^~~^-~^~^~^-~^~^~^~^~~^~-`";
        public static final String NIGHT = "          _.._\r\n        .' .-'`\r\n       /  /\r\n       |  |\r\n       \\  '.___.;\r\n         '._  _.'\r\n           ``";
        public static final String INSTRUCTIONS ="GAME INSTRUCTIONS:\r\n\r\n/play - To start the game\r\n/whisper - You can whisper other players and talk secretly.\r\n/list - List all players in the game\r\n";
        public static final String WEREWOLF = StringColor.ANSI_RED_BACKGROUND+ "YOU ARE THE WOLF!" + StringColor.ANSI_RESET + StringColor.ANSI_RED + "\r\n            \r\n                            .d$$b\r\n                          .' TO$;\\\r\n                         /  : TP._;\r\n                        / _.;  :Tb|\r\n                       /   /   ;j$j\r\n                   _.-\"       d$$$$\r\n                 .' ..       d$$$$;\r\n                /  /P'      d$$$$P. |\\\r\n               /   \"      .d$$$P' |\\^\"l\r\n             .'           `T$P^\"\"\"\"\"  :\r\n         ._.'      _.'                ;\r\n      `-.-\".-'-' ._.       _.-\"    .-\"\r\n    `.-\" _____  ._              .-\"\r\n   -(.g$$$$$$$b.              .'\r\n     \"\"^^T$$$P^)            .(:\r\n       _/  -\"  /.'         /:/;\r\n    ._.'-'`-'  \")/         /;/;\r\n `-.-\"..--\"\"   \" /         /  ;\r\n.-\" ..--\"\"        -'          :\r\n..--\"\"--.-\"         (\\      .-(\\\r\n  ..--\"\"              `-\\(\\/;`\r\n    _.                      :\r\n                            ;`-\r\n                           :\\\r\n                           " + StringColor.ANSI_RESET;
        public static final String KILL = "WHO DO YOU WANT TO KILL?";
        public static final String DAY ="AVAILABLE DAY COMMANDS:\r\n\r\n/vote - To start voting\r\n/whisper - You can whisper other players and talk secretly.\r\n/list - List all players in the game\r\n";

        public static final String GUESSRIGHT = StringColor.ANSI_GREEN_BACKGROUND + "YOU FOUND THE WEREWOLF!" + StringColor.ANSI_RESET;
        public static final String GUESSWRONG = StringColor.ANSI_RED_BACKGROUND + "YOUR GUESS WAS WRONG!" + StringColor.ANSI_RESET;
        public static final String PSYCHICPHRASE = "DO YOU HAVE AN ESOTERIC PERCEPTION OF ANYONE?";
        public static final String PSYCHIC =StringColor.ANSI_YELLOW_BACKGROUND + "YOU ARE THE PSYCHIC!" + StringColor.ANSI_RESET + StringColor.ANSI_YELLOW + "\r\n            _ . - = - . _\r\n       . \"  \\  \\   /  /  \" .\r\n     ,  \\                 /  .\r\n   . \\   _,.--~=~\"~=~--.._   / .\r\n  ;  _.-\"  / \\ !   ! / \\  \"-._  .\r\n / ,\"     / ,` .---. `, \\     \". \\\r\n/.'   `~  |   /:::::\\   |  ~`   '.\\\r\n\\`.  `~   |   \\:::::/   | ~`  ~ .'/\r\n \\ `.  `~ \\ `, `~~~' ,` /   ~`.' /\r\n  .  \"-._  \\ / !   ! \\ /  _.-\"  .\r\n   ./    \"=~~.._  _..~~=`\"    \\.\r\n     ,/         \"\"          \\,\r\n       . _/             \\_ .\r\n          \" - ./. .\\. - \"" + StringColor.ANSI_RESET;

        public static final String PRIEST = StringColor.ANSI_BLUE_BACKGROUND +"YOU ARE THE PRIEST!" + StringColor.ANSI_RESET + StringColor.ANSI_BLUE +"\r\n      _.--._\r\n      \\ ** /\r\n       (<>)\r\n.      )  (      .\r\n)\\_.._/ /\\ \\_.._/(\r\n(*_<>_  _<>_*)\r\n)/ '' \\ \\/ / '' \\(\r\n'      )  (      '\r\n       (  ) \r\n       )  (\r\n       (<>)\r\n      / ** \\\r\n     /.-..-.\\" + StringColor.ANSI_RESET;
        public static final String PRIESTPHRASE = "WHO DO YOU WANT TO SAVE?";

        public static final String VILLAGER = StringColor.ANSI_GREEN_BACKGROUND +"YOU ARE THE VILLAGER!" + StringColor.ANSI_RESET + StringColor.ANSI_GREEN +"\r\n   .------\\ /------.\r\n   |       -       |\r\n   |               |\r\n   |               |\r\n   |               |\r\n_______________________\r\n===========.===========\r\n  / ~~~~~     ~~~~~ \\\r\n /|     |     |\\\r\n W   ---  / \\  ---   W\r\n \\.      |o o|      ./\r\n  |                 |\r\n  \\    #########    /\r\n   \\  ## ----- ##  /\r\n    \\##         ##/\r\n     \\_____v_____/" + StringColor.ANSI_RESET;
        public static final String VILLAGERPHRASE="What a beautiful night. I should get some sleep.";
        public static final String VOTING = "WHO DO YOU WANT TO LYNCH?";
        public static final String WOLFWON = "THE WOLF WON THE GAME! THE GAME ENDED.";
        public static final String VILLAGEWON = "THE WOLF WAS SLAYED, THE VILLAGE IS SAFE! THE GAME ENDED.";
        public static final String DEAD = "YOU ARE DEAD. Go drink a coffee.";
        public static final String GOODDAY = "TODAY WAS A GOOD DAY, NOBODY DIED.\n";
        public static final String VOTINGINC = "VOTING WAS INCONCLUSIVE, NOBODY DIED\n";


        public static final String ENTER = "Press enter to skip night.";
        public static final String GAMEFULL=StringColor.ANSI_RED_BACKGROUND + "Game is full, try again later!" + StringColor.ANSI_RESET;
        public static final String WAITING = "Waiting for players, please stand by...";
        public static final String WHISPER = "Who do you want to send the secret message?";
        public static final String MESSAGE = "What is the message ?";
        public static final String USER = StringColor.ANSI_RED_BACKGROUND + "That user does not exist!" + StringColor.ANSI_RESET;

}
