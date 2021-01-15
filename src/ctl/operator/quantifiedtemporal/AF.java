package ctl.operator.quantifiedtemporal;

import ctl.CTLFormula;

import java.util.Objects;

import static ctl.atom.True.True;

/**
 * Classe permettant de représenter une formule CTL de la forme AF(phi).
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class AF implements CTLFormula {
    private final CTLFormula operand;

    /**
     * Constructeur permettant d'instancier une formule CTL de type AF(phi).
     *
     * @param operand phi
     */
    public AF(CTLFormula operand) {
        this.operand = operand;
    }

    @Override
    public CTLFormula toCTL() {
        return new AU(True(), operand).toCTL();
    }

    @Override
    public String toString() {
        return "AF(" + operand.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AF that = (AF) o;
        return Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand);
    }
}
