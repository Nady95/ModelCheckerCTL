package ctl.operator.bool;

import ctl.CTLFormula;

import java.util.Objects;

/**
 * Classe permettant de représenter une formule CTL de la forme Not(phi).
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class Not implements CTLFormula {
    private final CTLFormula operand;

    /**
     * Constructeur permettant d'instancier une formule CTL de la forme Not(phi).
     *
     * @param operand phi
     */
    public Not(CTLFormula operand) {
        this.operand = operand;
    }

    /**
     * Getter retournant l'opérande (phi) de cette formule.
     *
     * @return phi
     */
    public CTLFormula getOperand() {
        return operand;
    }

    @Override
    public CTLFormula toCTL() {
        return new Not(operand.toCTL());
    }

    @Override
    public String toString() {
        return "NOT " + operand.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Not that = (Not) o;
        return Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand);
    }
}
