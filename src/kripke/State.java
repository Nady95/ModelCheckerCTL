package kripke;

import ctl.CTLFormula;

import java.util.ArrayList;
import java.util.List;

public class State {
    private final String name;
    private final ArrayList<CTLFormula> labels;
    private final ArrayList<State> linkedStates;
    private final ArrayList<State> parentStates;
    private final boolean isInitial;
    private int nb;

    public State(String name, boolean isInitial, List<CTLFormula> atomicPropositions) {
        this.name = name;
        this.isInitial = isInitial;
        this.labels = new ArrayList<>();
        this.labels.addAll(atomicPropositions);
        this.linkedStates = new ArrayList<>();
        this.parentStates = new ArrayList<>();
        this.nb = 0;
    }

    public void addLinkedState(State state) {
        linkedStates.add(state);
        state.addParentState(this);
        this.nb++;
    }

    public void addParentState(State state) {
        parentStates.add(state);
    }

    public String getName() {
        return this.name;
    }

    public void decrementNb() {
        this.nb--;
    }

    public void resetNb() {
        this.nb = linkedStates.size();
    }

    public boolean isInitial() {
        return isInitial;
    }

    public ArrayList<CTLFormula> getLabels() {
        return this.labels;
    }

    public ArrayList<State> getLinkedStates() {
        return this.linkedStates;
    }

    public ArrayList<State> getParentStates() {
        return this.parentStates;
    }

    public int getNb() {
        return this.nb;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return name.equals(((State) obj).getName());
    }

    @Override
    public String toString() {
        return this.name;
    }
}
