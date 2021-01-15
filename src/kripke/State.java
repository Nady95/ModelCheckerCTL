package kripke;

import ctl.CTLFormula;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un état d'une structure de Kripke. Ce dernier possède un nom, des états enfants/parents,
 * vérifie des formules atomiques (labels) et peut être initial.
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class State {
    private final String name;
    private final ArrayList<CTLFormula> labels;
    private final ArrayList<State> childStates;
    private final ArrayList<State> parentStates;
    private final boolean isInitial;
    private int nbChild;

    /**
     * Constructeur permettant d'instancier un nouvel état d'une structure de Kripke.
     *
     * @param name               Nom de l'état
     * @param isInitial          Booléen indiquant s'il est initial
     * @param atomicPropositions Liste de propositions atomiques (labels) vérifiées par l'état
     */
    public State(String name, boolean isInitial, List<CTLFormula> atomicPropositions) {
        this.name = name;
        this.isInitial = isInitial;
        this.labels = new ArrayList<>();
        this.labels.addAll(atomicPropositions);
        this.childStates = new ArrayList<>();
        this.parentStates = new ArrayList<>();
        this.nbChild = 0;
    }

    /**
     * Méthode permettant d'ajouter une nouvelle transition sortante entre cet état, et un autre.
     *
     * @param state Autre état qui deviendra un état enfant de celui-ci
     */
    public void addChildState(State state) {
        childStates.add(state);
        state.addParentState(this);
        this.nbChild++;
    }

    /**
     * Méthode permettant d'ajouter un état à la liste des parents de cet état. Cette méthode est utilisée dans
     * addChildState(State).
     *
     * @param state Etat parent
     */
    public void addParentState(State state) {
        parentStates.add(state);
    }

    /**
     * Méthode indiquant si cet état vérifie une proposition atomique (càd si sa liste de labels contient celui donné
     * en paramètre).
     *
     * @param label Proposition atomique à vérifier
     * @return Vrai si elle est vérifiée, faux sinon
     */
    public boolean hasLabel(CTLFormula label) {
        return labels.contains(label);
    }

    /**
     * Getter retournant le nom de l'état
     *
     * @return Nom de l'état sous forme de chaîne de caractères
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter retournant le nombre d'états enfants de celui-ci.
     *
     * @return Nombre d'enfants de cet état sous forme d'un entier
     */
    public int getNbChild() {
        return this.nbChild;
    }

    /**
     * Getter retournant la liste des états enfants de celui-ci.
     *
     * @return Liste des enfants de cet état
     */
    public ArrayList<State> getChildStates() {
        return this.childStates;
    }

    /**
     * Getter retournant la liste des états parents de celui-ci.
     *
     * @return Liste des parents de cet état
     */
    public ArrayList<State> getParentStates() {
        return this.parentStates;
    }

    /**
     * Getter indiquant si cet état est initial.
     *
     * @return Vrai s'il l'est, faux sinon
     */
    public boolean isInitial() {
        return isInitial;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return name.equals(((State) obj).getName());
    }

    @Override
    public String toString() {
        return this.name;
    }
}
