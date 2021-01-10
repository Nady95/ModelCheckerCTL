package ctl.operator.quantifiedtemporal;

import ctl.CTLFormula;
import ctl.operator.bool.Not;

import java.util.Objects;

public class AG implements CTLFormula {
    private final CTLFormula operand;

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
}
