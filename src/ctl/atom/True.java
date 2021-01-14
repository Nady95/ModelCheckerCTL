package ctl.atom;

import ctl.CTLFormula;

public final class True implements CTLFormula {
    private static final True INSTANCE = new True();

    public True() {
    }

    public static True getInstance() {
        return new True();
    }

    @Override
    public CTLFormula toCTL() {
        return this;
    }

    @Override
    public String toString() {
        return "true";
    }

    @Override
    public boolean equals(Object o) {
        return INSTANCE.toString().equals(o.toString()) || (INSTANCE.getClass() == o.getClass());
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
