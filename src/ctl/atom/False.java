package ctl.atom;

import ctl.CTLFormula;

public class False implements CTLFormula {

    public False() {
    }

    @Override
    public CTLFormula toCTL() {
        return new False();
    }

    @Override
    public String toString() {
        return "False";
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (getClass() == o.getClass());
    }

}
