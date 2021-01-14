package ctl;

import kripke.State;

public class StateWrapper {
    private State state;
    private Boolean verifiesCTL;

    public StateWrapper(State state) {
        this.state = state;
        this.verifiesCTL = null;
    }

    public State getState() {
        return state;
    }

    public String getStateName() {
        return state.getName();
    }

    public Boolean getVerifiesCTL() {
        return verifiesCTL;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setVerifiesCTL(Boolean verifiesCTL) {
        this.verifiesCTL = verifiesCTL;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return getStateName().equals(((StateWrapper) obj).getStateName());
    }
}
