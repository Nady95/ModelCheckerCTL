package kripke;

import java.util.*;

/**
 * Classe représentant une structure de Kripke. Elle est composée d'une liste d'états et possède des transitions
 * entre ces derniers.
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class KripkeStructure {
    private final Map<String, State> states;

    /**
     * Constructeur permettant d'instancier une nouvelle structure de Kripke.
     */
    public KripkeStructure() {
        states = new HashMap<>();
    }

    /**
     * Méthode permettant d'ajouter des états à une cette structure de Kripke.
     *
     * @param states Etats à ajouter
     */
    public void addStates(State... states) {
        for(State s : states) {
            this.states.put(s.getName(), s);
        }
    }

    /**
     * Méthode permettant d'ajouter une transition entre deux états de cette structure de Kripke.
     *
     * @param state1 Etat parent
     * @param state2 Etat enfant
     */
    public void addTransition(State state1, State state2) {
        state1.addChildState(state2);
    }

    /**
     * Getter retournant une table de hashage des états de cette structure. La clé est le nom de l'état.
     *
     * @return Table de hashage des états de la structure, avec pour clé le nom de l'état
     */
    public Map<String, State> getStates() {
        return states;
    }

    /**
     * Getter permettant de retourner un état de cette structure grâce à son nom.
     *
     * @param name Nom de l'état à chercher
     * @return Etat correspondant
     */
    public State getStateFromName(String name) {
        return states.get(name);
    }
}
