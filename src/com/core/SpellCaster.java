package com.core;

import java.util.ArrayList;
import java.util.List;

public class SpellCaster {

    public SpellCaster(){

    }

    private List<SpellListener> listeners = new ArrayList<SpellListener>();

    public void addListener(SpellListener toAdd) {
        listeners.add(toAdd);
    }

    public void castSpell(int id) {
        // Notify everybody that may be interested.
        for (SpellListener sp : listeners)
            sp.spellCasted(id);
    }
}
