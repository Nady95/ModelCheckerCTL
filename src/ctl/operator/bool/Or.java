package ctl.operator.bool;

import ctl.CTLFormula;

import java.util.Objects;

/**
 * Classe permettant de représenter une formule CTL de la forme phi1 OR phi2.
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class Or implements CTLFormula {
    private final CTLFormula operand1;
    private final CTLFormula operand2;

    /**
     * Constructeur permettant d'instancier une formule CTL de la forme phi1 OR phi2.
     *
     * @param operand1 phi1
     * @param operand2 phi2
     */
    public Or(CTLFormula operand1, CTLFormula operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    @Override
    public CTLFormula toCTL() {
        return new Not(new And(new Not(operand1), new Not(operand2)));
    }

    @Override
    public String toString() {
        return "(" + operand1.toString() + " OR " + operand2.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Or that = (Or) o;
        return Objects.equals(operand1, that.operand1) && Objects.equals(operand2, that.operand2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand1, operand2);
    }
}
