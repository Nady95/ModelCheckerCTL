package ctl.operator.quantifiedtemporal;

import ctl.CTLFormula;
import ctl.atom.True;

import java.util.Objects;

public class AF implements CTLFormula {
    private final CTLFormula operand;

    public AF(CTLFormula operand) {
        this.operand = operand;
    }

    @Override
    public CTLFormula toCTL() {
        return new AU(new True(), operand).toCTL();
    }

    @Override
    public String toString() {
        return "AF(" + operand.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AF that = (AF) o;
        return Objects.equals(operand, that.operand);
    }
}
