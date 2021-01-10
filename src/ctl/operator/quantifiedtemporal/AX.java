package ctl.operator.quantifiedtemporal;

import ctl.CTLFormula;

import java.util.Objects;

public class AX implements CTLFormula {
    private final CTLFormula operand;

    public AX(CTLFormula operand) {
        this.operand = operand;
    }

    @Override
    public CTLFormula toCTL() {
        return new AX(operand.toCTL());
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
}
