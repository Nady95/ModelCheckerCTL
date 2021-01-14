package ctl.operator.quantifiedtemporal;

import ctl.CTLFormula;
import ctl.atom.True;

import java.util.Objects;

public class EF implements CTLFormula {
    private final CTLFormula operand;

    public EF(CTLFormula operand) {
        this.operand = operand;
    }

    @Override
    public CTLFormula toCTL() {
        return new EU(new True(), operand).toCTL();
    }

    public CTLFormula getOperand() {
        return operand;
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
