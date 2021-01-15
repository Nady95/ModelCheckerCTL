package ctl.operator.quantifiedtemporal;

import ctl.CTLFormula;
import ctl.operator.bool.Not;

import java.util.Objects;

/**
 * Classe permettant de représenter une formule CTL de la forme AX(phi).
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class AX implements CTLFormula {
    private final CTLFormula operand;

    /**
     * Constructeur permettant d'instancier une formule CTL de type AX(phi).
     *
     * @param operand phi
     */
    public AX(CTLFormula operand) {
        this.operand = operand;
    }

    @Override
    public CTLFormula toCTL() {
        return new Not(new EX(new Not(operand))).toCTL();
    }

    @Override
    public String toString() {
        return "AX(" + operand.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AX that = (AX) o;
        return Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand);
    }
}
