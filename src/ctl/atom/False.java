package ctl.atom;

import ctl.CTLFormula;

/**
 * Classe permettant de représenter le label Faux (qui est une formule CTL).
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class False implements CTLFormula {
    private static final False INSTANCE = new False();

    /**
     * Constructeur vide, permettant d'instancier une unique fois le label Faux.
     */
    public False() {
    }

    /**
     * Méthode statique permettant de retourner l'unique instance de la classe False.
     *
     * @return Instance unique de la classe False.
     */
    public static False False() {
        return INSTANCE;
    }

    @Override
    public CTLFormula toCTL() {
        return this;
    }

    @Override
    public String toString() {
        return "false";
    }

    @Override
    public boolean equals(Object o) {
        return INSTANCE.toString().equals(o.toString()) || (INSTANCE.getClass() == o.getClass());
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
