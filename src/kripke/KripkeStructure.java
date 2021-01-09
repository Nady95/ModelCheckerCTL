package kripke;

import java.util.*;

public class KripkeStructure {
    private final Map<String, State> states;

    public KripkeStructure() {
        states = new HashMap<>();
    }

    public void addStates(State... states) {
        for(State s : states) {
            this.states.put(s.getName(), s);
        }
    }

    public void addTransition(State state1, State state2) {
        state1.addLinkedState(state2);
    }


    public Map<String, State> getStates() {
        return states;
    }

    public State getStateFromName(String name) {
        return states.get(name);
    }
}
