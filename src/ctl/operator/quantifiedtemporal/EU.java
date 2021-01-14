package ctl.operator.quantifiedtemporal;

import ctl.CTLFormula;

import java.util.Objects;

public class EU implements CTLFormula {
    private final CTLFormula operand1;
    private final CTLFormula operand2;

    public EU(CTLFormula operand1, CTLFormula operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    @Override
    public CTLFormula toCTL() {
        return new EU(operand1.toCTL(), operand2.toCTL());
    }

    public CTLFormula getOperand1() {
        return operand1;
    }

    public CTLFormula getOperand2() {
        return operand2;
    }

    @Override
    public String toString() {
        return "E(" + operand1.toString() + " U " + operand2.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EU that = (EU) o;
        return Objects.equals(operand1, that.operand1) &&
                Objects.equals(operand2, that.operand2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand1, operand2);
    }
}
