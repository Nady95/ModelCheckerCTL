package kripke;

import java.util.ArrayList;
import java.util.Arrays;

public class KripkeStructure {
    private final ArrayList<State> states;
    private final ArrayList<State> initialStates;

    public KripkeStructure() {
        states = new ArrayList<>();
        initialStates = new ArrayList<>();
    }

    public void addStates(State... states) {
        this.states.addAll(Arrays.asList(states));
    }

    public void addInitialStates(State... states) {
        this.initialStates.addAll(Arrays.asList(states));
    }

    public void addTransition(State state1, State state2) {
        state1.addLinkedState(state2);
    }
}
