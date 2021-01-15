package ctl.operator.bool;

import ctl.CTLFormula;

import java.util.Objects;

/**
 * Classe permettant de représenter une formule CTL de la forme phi1 INTER phi2.
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class And implements CTLFormula {
    private final CTLFormula operand1;
    private final CTLFormula operand2;

    /**
     * Constructeur permettant d'instancier une formule CTL de la forme phi1 INTER phi2.
     *
     * @param operand1 phi1
     * @param operand2 phi2
     */
    public And(CTLFormula operand1, CTLFormula operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    /**
     * Getter retournant la première opérande (= phi1) de cette formule.
     *
     * @return phi1
     */
    public CTLFormula getOperand1() {
        return operand1;
    }

    /**
     * Getter retournant la deuxième opérande (= phi2) de cette formule.
     *
     * @return phi2
     */
    public CTLFormula getOperand2() {
        return operand2;
    }

    @Override
    public CTLFormula toCTL() {
        return new And(operand1.toCTL(), operand2.toCTL());
    }

    @Override
    public String toString() {
        return "(" + operand1.toString() + " AND " + operand2.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        And that = (And) o;
        return Objects.equals(operand1, that.operand1) && Objects.equals(operand2, that.operand2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand1, operand2);
    }
}
