package ctl;

/**
 * Interface représentant une formule CTL. Celle-ci doit pouvoir être convertie en formule CTL de base (donc simplifiée).
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public interface CTLFormula {
    /**
     * Methode permettant de convertir une formule CTL en formule CTL de base (donc qui n'utilise que certains
     * opérateurs). E.g. : AF(phi) = A(true U phi)
     *
     * @return Formule CTL de base
     */
    CTLFormula toCTL();
}
