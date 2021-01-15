package ctl.operator.quantifiedtemporal;

import ctl.CTLFormula;

import java.util.Objects;

/**
 * Classe permettant de représenter une formule CTL de la forme EX(phi).
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class EX implements CTLFormula {
    private final CTLFormula operand;

    /**
     * Constructeur permettant d'instancier une formule CTL de type EX(phi).
     *
     * @param operand phi
     */
    public EX(CTLFormula operand) {
        this.operand = operand;
    }

    @Override
    public CTLFormula toCTL() {
        return new EX(operand.toCTL());
    }

    public CTLFormula getOperand() {
        return operand;
    }

    @Override
    public String toString() {
        return "EX(" + operand.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EX that = (EX) o;
        return Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand);
    }
}
