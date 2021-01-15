package ctl.operator.quantifiedtemporal;

import ctl.CTLFormula;
import ctl.operator.bool.Not;

import java.util.Objects;

/**
 * Classe permettant de représenter une formule CTL de la forme AG(phi).
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class AG implements CTLFormula {
    private final CTLFormula operand;

    /**
     * Constructeur permettant d'instancier une formule CTL de type AG(phi).
     *
     * @param operand phi
     */
    public AG(CTLFormula operand) {
        this.operand = operand;
    }

    @Override
    public CTLFormula toCTL() {
        return new Not(new EF(new Not(operand))).toCTL();
    }

    @Override
    public String toString() {
        return "AG(" + operand.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AG that = (AG) o;
        return Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand);
    }

}
