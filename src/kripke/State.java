package kripke;

import java.util.ArrayList;
import java.util.List;

public class State {
    private final String name;
    private final ArrayList<String> labels;
    private final ArrayList<State> linkedStates;
    private final boolean isInitial;

    public State(String name, boolean isInitial, List<String> atomicPropositions) {
        this.name = name;
        this.isInitial = isInitial;
        this.labels = new ArrayList<>();
        this.labels.addAll(atomicPropositions);
        this.linkedStates = new ArrayList<>();
    }

    public void addLinkedState(State state) {
        linkedStates.add(state);
    }

    public String getName() {
        return this.name;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public ArrayList<String> getLabels() {
        return this.labels;
    }

    public ArrayList<State> getLinkedStates() {
        return this.linkedStates;
    }
}
