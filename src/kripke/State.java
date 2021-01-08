package kripke;

import java.util.ArrayList;
import java.util.Collections;

public class State {
    private final String name;
    private final ArrayList<String> atomicPropositions;
    private final ArrayList<State> linkedStates;

    public State(String name, String... atomicPropositions) {
        this.name = name;
        this.atomicPropositions = new ArrayList<>();
        Collections.addAll(this.atomicPropositions, atomicPropositions);
        this.linkedStates = new ArrayList<>();
    }

    public void addLinkedState(State state) {
        linkedStates.add(state);
    }
}
