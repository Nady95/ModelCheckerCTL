package ctl.operator.bool;

import ctl.CTLFormula;

import java.util.Objects;

public class Not implements CTLFormula {
    private final CTLFormula operand;

    public Not(CTLFormula operand) {
        this.operand = operand;
    }

    @Override
    public CTLFormula toCTL() {
        return new Not(operand.toCTL());
    }

    public CTLFormula getOperand() {
        return operand;
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
