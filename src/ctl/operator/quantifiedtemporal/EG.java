package ctl.operator.quantifiedtemporal;

import ctl.CTLFormula;
import ctl.operator.bool.Not;

import java.util.Objects;

public class EG implements CTLFormula {
    private final CTLFormula operand;

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
}
