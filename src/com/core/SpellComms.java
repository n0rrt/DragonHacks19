package com.core;

import java.util.ArrayList;
import java.util.List;

public class SpellComms {

}

// An interface to be implemented by everyone interested in "Hello" events


// Someone who says "Hello"


// Someone interested in "Hello" events
//abstract class SpellListener implements SpellListenerI {
//    @Override
//    public void spellCasted(int id) {
//        System.out.println(id);
//    }
//}

class Test {
    public static void main(String[] args) {
        SpellCaster caster = new SpellCaster();

        caster.addListener(new SpellListener() {
            @Override
            public void spellCasted(int id) {

            }
        });

        caster.castSpell(3);  // Prints "Hello!!!" and "Hello there..."
    }
}
