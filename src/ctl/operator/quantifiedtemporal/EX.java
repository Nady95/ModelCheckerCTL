package ctl.operator.quantifiedtemporal;

import ctl.CTLFormula;

import java.util.Objects;

public class EX implements CTLFormula {
    private final CTLFormula operand;

    public EX(CTLFormula operand) {
        this.operand = operand;
    }

    @Override
    public CTLFormula toCTL() {
        return new EX(operand.toCTL());
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

}
