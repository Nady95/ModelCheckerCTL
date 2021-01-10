package ctl.atom;

import ctl.CTLFormula;

import java.util.Objects;

public class Atom implements CTLFormula {
    private final String atomicProposition;

    public Atom(String atomicProposition) {
        this.atomicProposition = atomicProposition;
    }

    @Override
    public CTLFormula toCTL() {
        return this;
    }

    @Override
    public String toString() {
        return this.atomicProposition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Atom that = (Atom) o;
        return Objects.equals(atomicProposition, that.atomicProposition);
    }
}
