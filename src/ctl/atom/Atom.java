package ctl.atom;

import ctl.CTLFormula;

import java.util.Objects;

/**
 * Classe permettant de représenter une proposition atomique (qui est une formule CTL).
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class Atom implements CTLFormula {
    private final String atomicProposition;

    /**
     * Constructeur permettant d'instancier une proposition atomique à partir d'une chaîne de caractères
     *
     * @param atomicProposition Proposition atomique sous la forme d'une chaîne de caractères
     */
    public Atom(String atomicProposition) {
        this.atomicProposition = atomicProposition;
    }

    @Override
    public CTLFormula toCTL() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Atom that = (Atom) o;
        return Objects.equals(atomicProposition, that.atomicProposition);
    }

    @Override
    public String toString() {
        return this.atomicProposition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(atomicProposition);
    }
}
