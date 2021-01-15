package ctl.operator.quantifiedtemporal;

import ctl.CTLFormula;
import ctl.operator.bool.Not;

import java.util.Objects;

/**
 * Classe permettant de représenter une formule CTL de la forme EG(phi).
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class EG implements CTLFormula {
    private final CTLFormula operand;

    /**
     * Constructeur permettant d'instancier une formule CTL de type EG(phi).
     *
     * @param operand phi
     */
    public EG(CTLFormula operand) {
        this.operand = operand;
    }

    @Override
    public CTLFormula toCTL() {
        return new Not(new AF(new Not(operand))).toCTL();
    }

    @Override
    public String toString() {
        return "EG(" + operand.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EG that = (EG) o;
        return Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand);
    }
}
