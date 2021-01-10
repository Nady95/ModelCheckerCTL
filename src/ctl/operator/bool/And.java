package ctl.operator.bool;

import ctl.CTLFormula;

import java.util.Objects;

public class And implements CTLFormula {
    private final CTLFormula operand1;
    private final CTLFormula operand2;

    public And(CTLFormula operand1, CTLFormula operand2) {
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    @Override
    public CTLFormula toCTL() {
        return new And(operand1.toCTL(), operand2.toCTL());
    }

    @Override
    public String toString() {
        return operand1.toString() + " AND " + operand2.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        And that = (And) o;
        return Objects.equals(operand1, that.operand1) && Objects.equals(operand2, that.operand2);
    }
}
