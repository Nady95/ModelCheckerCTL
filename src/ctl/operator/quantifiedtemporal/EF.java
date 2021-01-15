package ctl.operator.quantifiedtemporal;

import ctl.CTLFormula;

import java.util.Objects;

import static ctl.atom.True.True;

/**
 * Classe permettant de représenter une formule CTL de la forme EF(phi).
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class EF implements CTLFormula {
    private final CTLFormula operand;

    /**
     * Constructeur permettant d'instancier une formule CTL de type EF(phi).
     *
     * @param operand phi
     */
    public EF(CTLFormula operand) {
        this.operand = operand;
    }

    @Override
    public CTLFormula toCTL() {
        return new EU(True(), operand).toCTL();
    }

    @Override
    public String toString() {
        return "EF(" + operand.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EF that = (EF) o;
        return Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand);
    }
}
