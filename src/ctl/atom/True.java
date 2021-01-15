package ctl.atom;

import ctl.CTLFormula;

/**
 * Classe permettant de représenter le label True (qui est une formule CTL).
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public final class True implements CTLFormula {
    private static final True INSTANCE = new True();

    /**
     * Constructeur vide, permettant d'instancier une unique fois le label True.
     */
    public True() {
    }

    /**
     * Méthode statique permettant de retourner l'unique instance de la classe True.
     *
     * @return Instance unique de la classe True.
     */
    public static True True() {
        return INSTANCE;
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
