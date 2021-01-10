package ctl.atom;

import ctl.CTLFormula;

public class True implements CTLFormula {

    public True() {
    }

    @Override
    public CTLFormula toCTL() {
        return new True();
    }

    @Override
    public String toString() {
        return "True";
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (getClass() == o.getClass());
    }
}
