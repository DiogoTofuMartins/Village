package org.academiadecodigo.bitjs.village.characters;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CharacterFactory {

    private static List<Character> list;

    public static void init(int number) {

        list = Collections.synchronizedList(new LinkedList<>());
        list.add(new Werewolf());
        list.add(new Priest());
        list.add(new Psychic());

        for (int i = 0; i < number - 3; i++) {
            list.add(new Villager());
        }
    }

    public static List<Character> getList(int numberOfCharacters) {

        init(numberOfCharacters);
        return list;
    }

}