import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ctl.CTLFormula;
import ctl.atom.Atom;
import ctl.atom.False;
import ctl.atom.True;
import ctl.operator.bool.And;
import ctl.operator.bool.Not;
import ctl.operator.bool.Or;
import ctl.operator.quantifiedtemporal.*;
import kripke.KripkeStructure;
import kripke.State;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static ctl.atom.False.False;
import static ctl.atom.True.True;

/**
 * Classe principale de l'application "Model Checker CTL", réalisée dans le cadre d'un projet de Systèmes complexes
 * en Master 2 PLS à l'Institut Galilée.
 * <p>
 * Cette application a pour but de permettre à l'utilisateur de soumettre une structure de Kripke accompagnée d'une
 * formule CTL, et de vérifier quels états de la structure la vérifie. Ces derniers sont fournis via un fichier json
 * et la syntaxe à suivre est indiquée dans le README.md disponible ici :
 * https://github.com/Nady95/ModelCheckerCTL/blob/master/README.md
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class ModelCheckerCTL {
    private static final KripkeStructure kripkeStructure = new KripkeStructure();
    private static CTLFormula ctlFormula;


    /**
     * Méthode principale de notre programme : elle se charge récupérer le fichier passé par l'utilisateur, et appelle
     * les fonctions permettant d'instancier la structure de Kripke ainsi que la formule CTL, et de procéder à la
     * vérification de cette dernière
     *
     * @param args Texte passé en argument dans la console (seul args[0] est lu et devrait être un chemin vers un
     *             fichier json)
     */
    public static void main(String[] args) {
        if (args.length <= 0) {
            throw new IllegalArgumentException("Il faut donner en paramètre un lien vers un fichier contenant " +
                    "une description textuelle d'une structure de Kripke (KS) et une formule CTL");
        } else {
            try {
                readFile(args[0]);
                CTLFormula baseFormula = ctlFormula.toCTL();
                checkCTLSatisfaction(baseFormula);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Méthode se chargeant de lire le fichier passé en paramètre, et d'appeler toutes les fonctions permettant
     * de créer la structure de Kripke et la fonction CTL à tester grâce au contenu du fichier lu
     *
     * @param filePath Lien absolu vers le fichier json à lire
     * @throws Exception Si le fichier n'a pas pu être lu correctement
     */
    public static void readFile(String filePath) throws Exception {
        try {
            // On récupère le contenu de notre fichier Json avec l'API Gson
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            // On crée chaque état depuis le fichier Json
            createStateFromJson(jsonObject.getAsJsonArray("states"));

            // On crée ensuite chaque transition
            createTransitionsFromJson(jsonObject.getAsJsonArray("transitions"));

            // Enfin, on récupère la formule CTL
            ctlFormula = parseFormulaFromJson(jsonObject.get("ctlformula").getAsString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode se chargeant d'instancier un état avec tous ses paramètres grâce à un tableau "states" d'un fichier Json,
     * et de l'ajouter à la structure de Kripke.
     *
     * @param jsonArray Tableau json contenant les différents états de la structure de Kripke
     */
    public static void createStateFromJson(JsonArray jsonArray) {
        for (JsonElement state : jsonArray) {

            // On récupère le nom de l'état
            String name = state.getAsJsonObject().get("name").getAsString();

            // On vérifie si c'est un état initial ou pas
            boolean isInitial = state.getAsJsonObject().get("isInitial").getAsBoolean();

            // On récupère les labels
            List<CTLFormula> labels = new ArrayList<>();
            JsonArray jLabels = state.getAsJsonObject().getAsJsonArray("labels");
            for (int i = 0; i < jLabels.size(); i++) {

                // On récupère le label dans le fichier Json sous la forme d'un JsonObject
                JsonObject jLabel = jLabels.get(i).getAsJsonObject();
                // Puis on récupère la formule atomique
                Atom atom = new Atom(jLabel.get("atom").getAsString());

                // Si la négation de la formule atomique a été demandée dans le Json, on crée CTLFormula avec Not
                if (jLabel.get("negation").getAsBoolean()) {
                    Not formula = new Not(atom);
                    labels.add(formula);
                }
                // Sinon, on crée une formule atomique classique
                else {
                    labels.add(atom);
                }
            }

            // Enfin, on crée l'état et on l'ajoute dans la structure de Kripke
            kripkeStructure.addStates(new State(name, isInitial, labels));
        }
    }

    /**
     * Méthode se chargeant de créer les transitions dans une structure de Kripke grâce à  un tableau "transitions"
     * d'un fichier json.
     *
     * @param jsonArray Tableau json contenant les différentes transitions de la structure de Kripke
     */
    public static void createTransitionsFromJson(JsonArray jsonArray) {
        for (JsonElement transition : jsonArray) {
            String stateAName = transition.getAsJsonObject().get("stateA").getAsString();
            String stateBName = transition.getAsJsonObject().get("stateB").getAsString();

            kripkeStructure.addTransition(
                    kripkeStructure.getStateFromName(stateAName),
                    kripkeStructure.getStateFromName(stateBName)
            );
        }
    }

    /**
     * Méthode récursive ayant pour rôle d'utiliser des expressions régulières afin de parser une formule CTL lu en
     * tant que chaîne de caractères, et de l'instancier en tant qu'un objet CTLFormula
     *
     * @param formulaAsString Formule CTL non parsée sous forme de chaîne de caractères
     * @return La formule CTL parsée et instanciée en tant qu'un objet CTLFormula
     * @throws Exception Indique que la formule donnée en paramètre est très certainement incorrecte
     */
    public static CTLFormula parseFormulaFromJson(String formulaAsString) throws Exception {
        // On retire tous les espaces et caractères invisibles
        String formulaTrim = formulaAsString.replaceAll("\\s+","");

        // On crée nos matchers qui vont parcourir le texte en cherchant le pattern
        Matcher MATCHER_ATOM = PatternList.ATOM.matcher(formulaTrim);
        Matcher MATCHER_FALSE = PatternList.FALSE.matcher(formulaTrim);
        Matcher MATCHER_TRUE = PatternList.TRUE.matcher(formulaTrim);

        Matcher MATCHER_AND = PatternList.AND.matcher(formulaTrim);
        Matcher MATCHER_NOT = PatternList.NOT.matcher(formulaTrim);
        Matcher MATCHER_OR = PatternList.OR.matcher(formulaTrim);

        Matcher MATCHER_AF = PatternList.AF.matcher(formulaTrim);
        Matcher MATCHER_AG = PatternList.AG.matcher(formulaTrim);
        Matcher MATCHER_AU = PatternList.AU.matcher(formulaTrim);
        Matcher MATCHER_AX = PatternList.AX.matcher(formulaTrim);

        Matcher MATCHER_EF = PatternList.EF.matcher(formulaTrim);
        Matcher MATCHER_EG = PatternList.EG.matcher(formulaTrim);
        Matcher MATCHER_EU = PatternList.EU.matcher(formulaTrim);
        Matcher MATCHER_EX = PatternList.EX.matcher(formulaTrim);

        // On cherche des matches (l'ordre des matchers est important)
        if (MATCHER_AF.matches()) {
            return new AF(parseFormulaFromJson(MATCHER_AF.group(2)));
        } else if (MATCHER_AG.matches()) {
            return new AG(parseFormulaFromJson(MATCHER_AG.group(2)));
        } else if (MATCHER_AU.matches()) {
            return new AU(parseFormulaFromJson(MATCHER_AU.group(3)), parseFormulaFromJson(MATCHER_AU.group(4)));
        } else if (MATCHER_AX.matches()) {
            return new AX(parseFormulaFromJson(MATCHER_AX.group(2)));
        } else if (MATCHER_EF.matches()) {
            return new EF(parseFormulaFromJson(MATCHER_EF.group(2)));
        } else if (MATCHER_EG.matches()) {
            return new EG(parseFormulaFromJson(MATCHER_EG.group(2)));
        } else if (MATCHER_EU.matches()) {
            return new EU(parseFormulaFromJson(MATCHER_EU.group(3)), parseFormulaFromJson(MATCHER_EU.group(4)));
        } else if (MATCHER_EX.matches()) {
            return new EX(parseFormulaFromJson(MATCHER_EX.group(2)));
        } else if (MATCHER_NOT.matches()) {
            return new Not(parseFormulaFromJson(MATCHER_NOT.group(2)));
        } else if (MATCHER_AND.matches()) {
            return new And(parseFormulaFromJson(MATCHER_AND.group(1)), parseFormulaFromJson(MATCHER_AND.group(3)));
        } else if (MATCHER_OR.matches()) {
            return new Or(parseFormulaFromJson(MATCHER_OR.group(1)), parseFormulaFromJson(MATCHER_OR.group(3)));
        } else if (MATCHER_TRUE.matches()) {
            return True();
        } else if (MATCHER_FALSE.matches()) {
            return False();
        } else if (MATCHER_ATOM.matches()) {
            return new Atom(MATCHER_ATOM.group());
        } else {
            throw new Exception("La formule CTL entrée dans le fichier Json est très certainement incorrecte. " +
                    "Veuillez vous référer au README.md pour connaître la syntaxe : " +
                    "https://github.com/Nady95/ModelCheckerCTL/blob/master/README.md");
        }
    }

    /**
     * Méthode se chargeant d'indiquer de manière textuelle quels sont les états d'une structure de Kripke qui
     * satisfont une formule CTL.
     *
     * @param formula Formule CTL à tester
     */
    public static void checkCTLSatisfaction(CTLFormula formula) {
        Map<String, Boolean> ctlVerifMap = getCtlVerifMap(formula);

        StringBuilder resultPrinted = new StringBuilder(
                "La formule CTL \"" + ctlFormula.toString() + "\" est vérifiée par les états : "
        );

        for (Map.Entry<String, Boolean> entry : ctlVerifMap.entrySet()) {
            String state = entry.getKey();
            Boolean verifiesCTL = entry.getValue();

            if (verifiesCTL) {
                resultPrinted.append(state).append(", ");
            }
        }

        System.out.println(resultPrinted);
    }

    /**
     * Méthode se chargeant de vérifier si la structure de Kripke créée satisfait une formule CTL en fonction du type
     * de cette dernière, et retourne une table de hashage contenant le nom de l'état pour clé, et un booléen comme
     * valeur.
     *
     * @param formula Formule CTL à tester
     * @return Table de hashage contenant le nom de l'état pour clé, et un booléen indiquant s'il vérifie la formule
     * CTL comme valeur
     */
    public static Map<String, Boolean> getCtlVerifMap(CTLFormula formula) {
        Map<String, Boolean> ctlVerifMap = new HashMap<>();

        if (formula instanceof Atom) {
            ctlVerifMap = marking((Atom) formula);
        } else if (formula instanceof Not) {
            ctlVerifMap = case1notPhi((Not) formula);
        } else if (formula instanceof And) {
            ctlVerifMap = case2And((And) formula);
        } else if (formula instanceof EX) {
            ctlVerifMap = case3EX((EX) formula);
        } else if (formula instanceof EU) {
            ctlVerifMap = case4EU((EU) formula);
        } else if (formula instanceof AU) {
            ctlVerifMap = case5AU((AU) formula);
        } else if (formula instanceof True) {
            ctlVerifMap = createCtlVerifMap();
            ctlVerifMap.replaceAll((state, bool) -> true);
        } else if (formula instanceof False) {
            ctlVerifMap = createCtlVerifMap();
        }

        return ctlVerifMap;
    }

    /**
     * Algorithme se chargeant de faire le marquage d'une structure de Kripke : elle regarde pour chaque état s'il
     * vérifie une formule atomique (label) et retourne une table de hashage contenant le nom de l'état pour clé, et un
     * booléen indiquant s'il la vérifie pour valeur.
     *
     * @param formula Formule (CTL) atomique à tester
     * @return Table de hashage contenant le nom de l'état pour clé, et un booléen indiquant s'il vérifie la formule
     * atomique pour valeur
     */
    public static Map<String, Boolean> marking(Atom formula) {
        Map<String, Boolean> marking = createCtlVerifMap();

        for (Map.Entry<String, Boolean> entry : marking.entrySet()) {
            String stateName = entry.getKey();

            State state = kripkeStructure.getStateFromName(stateName);
            if (state.hasLabel(formula) || state.hasLabel(True())) {
                entry.setValue(true);
            } else {
                entry.setValue(false);
            }
        }
        return marking;
    }

    /**
     * Algorithme se chargeant de traîter le cas où la formule CTL est de type Psi = Not(phi).
     *
     * @param formula Formule CTL de type Not(phi)
     * @return Table de hashage contenant le nom de l'état pour clé, et un booléen indiquant s'il vérifie la formule
     * CTL pour valeur
     */
    public static Map<String, Boolean> case1notPhi(Not formula) {
        CTLFormula operand = formula.getOperand();
        Map<String, Boolean> ctlVerifMap = getCtlVerifMap(operand);

        ctlVerifMap.replaceAll((state, bool) -> !bool);

        return ctlVerifMap;
    }


    /**
     * Algorithme se chargeant de traiter le cas où la formule CTL est de type Psi = phi1 INTER phi2.
     *
     * @param formula Formule CTL de type phi1 INTER phi2
     * @return Table de hashage contenant le nom de l'état pour clé, et un booléen indiquant s'il vérifie la formule
     * CTL pour valeur
     */
    public static Map<String, Boolean> case2And(And formula) {
        CTLFormula operand1 = formula.getOperand1();
        CTLFormula operand2 = formula.getOperand2();

        Map<String, Boolean> ctlVerifMapPhi1 = getCtlVerifMap(operand1);
        Map<String, Boolean> ctlVerifMapPhi2 = getCtlVerifMap(operand2);

        for (Map.Entry<String, Boolean> entry1 : ctlVerifMapPhi1.entrySet()) {
            String stateName = entry1.getKey();
            Boolean bool1 = entry1.getValue();
            Boolean bool2 = ctlVerifMapPhi2.get(stateName);
            ctlVerifMapPhi1.replace(stateName, bool1 && bool2);
        }

        return ctlVerifMapPhi1;
    }

    /**
     * Algorithme se chargeant de traiter le cas où la formule CTL est de type Psi = EX(phi)
     *
     * @param formula Formule CTL de type EX(phi)
     * @return Table de hashage contenant le nom de l'état pour clé, et un booléen indiquant s'il vérifie la formule
     * CTL pour valeur
     */
    public static Map<String, Boolean> case3EX(EX formula) {
        CTLFormula operand = formula.getOperand();
        Map<String, Boolean> ctlVerifMapPhi = getCtlVerifMap(operand);
        Map<String, Boolean> ctlVerifMap = createCtlVerifMap();

        for (Map.Entry<String, Boolean> entry : ctlVerifMap.entrySet()) {
            String stateName = entry.getKey();
            State state = kripkeStructure.getStateFromName(stateName);

            for (State childState : state.getChildStates()) {
                String childStateName = childState.getName();
                if (ctlVerifMapPhi.get(childStateName)) {
                    ctlVerifMap.replace(stateName, true);
                }
            }
        }

        return ctlVerifMap;
    }

    /**
     * Algorithme se chargeant de traiter le cas où la formule CTL est de type Psi = E(phi1 UNTIL phi2)
     *
     * @param formula Formule CTL de type E(phi1 UNTIL phi2)
     * @return Table de hashage contenant le nom de l'état pour clé, et un booléen indiquant s'il vérifie la formule
     * CTL pour valeur
     */
    public static Map<String, Boolean> case4EU(EU formula) {
        CTLFormula operand1 = formula.getOperand1();
        CTLFormula operand2 = formula.getOperand2();

        Map<String, Boolean> ctlVerifMapPhi1 = getCtlVerifMap(operand1);
        Map<String, Boolean> ctlVerifMapPhi2 = getCtlVerifMap(operand2);
        Map<String, Boolean> ctlVerifMap = createCtlVerifMap();

        Map<String, Boolean> statesSeenBefore = new HashMap<>();
        ArrayList<String> L = new ArrayList<>(kripkeStructure.getStates().size());


        for (State state : kripkeStructure.getStates().values()) {
            // On récupère le nom de l'état (qui sert d'identifiant)
            String stateName = state.getName();
            statesSeenBefore.put(stateName, false);
            if (ctlVerifMapPhi2.get(stateName)) {
                L.add(stateName);
            }
        }

        while (!L.isEmpty()) {
            String stateName = L.remove(L.size() - 1);
            State state = kripkeStructure.getStateFromName(stateName);
            ctlVerifMap.replace(stateName, true);

            for (State parentState : state.getParentStates()) {
                String parentStateName = parentState.getName();
                if (!statesSeenBefore.get(parentStateName)) {
                    statesSeenBefore.replace(parentStateName, true);
                    if (ctlVerifMapPhi1.get(parentStateName)) {
                        L.add(parentStateName);
                    }
                }
            }
        }

        return ctlVerifMap;
    }

    /**
     * Algorithme se chargeant de traiter le cas où la formule CTL est de type Psi = A(phi1 UNTIL phi2)
     *
     * @param formula Formule CTL de type A(phi1 UNTIL phi2)
     * @return Table de hashage contenant le nom de l'état pour clé, et un booléen indiquant s'il vérifie la formule
     * CTL pour valeur
     */
    public static Map<String, Boolean> case5AU(AU formula) {
        CTLFormula operand1 = formula.getOperand1();
        CTLFormula operand2 = formula.getOperand2();

        Map<String, Boolean> ctlVerifMapPhi1 = getCtlVerifMap(operand1);
        Map<String, Boolean> ctlVerifMapPhi2 = getCtlVerifMap(operand2);
        Map<String, Boolean> ctlVerifMap = createCtlVerifMap();

        ArrayList<String> L = new ArrayList<>(kripkeStructure.getStates().size());
        Map<String, Integer> degreeMap = createDegreeMap();

        for (Map.Entry<String, Boolean> entry : ctlVerifMapPhi2.entrySet()) {
            String stateName = entry.getKey();
            if (ctlVerifMapPhi2.get(stateName)) {
                L.add(stateName);
            }
        }

        while (!L.isEmpty()) {
            String stateName = L.remove(L.size() - 1);
            State state = kripkeStructure.getStateFromName(stateName);
            ctlVerifMap.replace(stateName, true);

            for (State parentState : state.getParentStates()) {
                String parentStateName = parentState.getName();
                degreeMap.merge(parentStateName, -1, Integer::sum);
                if (degreeMap.get(parentStateName) == 0 && ctlVerifMapPhi1.get(parentStateName) && !ctlVerifMap.get(parentStateName)) {
                    L.add(parentStateName);
                }
            }
        }

        return ctlVerifMap;
    }

    /**
     * Cette fonction crée une table de hashage qui assigne, pour chaque état de la structure de Kripke, son nom
     * (qui sert d'identifiant) à une valeur booléenne par défaut (false). Elle aura pour but d'indiquer pour chaque
     * état s'il vérifie une formule CTL.
     *
     * @return Une table de hashage par défaut
     */
    public static Map<String, Boolean> createCtlVerifMap() {
        final Map<String, State> stateMap = kripkeStructure.getStates();

        Map<String, Boolean> ctlVerifMap = new HashMap<>(stateMap.size());
        for (String state : stateMap.keySet()) {
            ctlVerifMap.put(state, false);
        }

        return ctlVerifMap;
    }

    /**
     * Cette méthode créer une table de hashage qui assigne, pour chaque état de la structure de Kripke, son nom
     * (qui sert d'identifiant) à son degré (soit son nombre d'état enfant).
     *
     * @return Table de hashage contenant (Nom_de_l_etat, degre_de_l_etat)
     */
    public static Map<String, Integer> createDegreeMap() {
        final Map<String, State> stateMap = kripkeStructure.getStates();

        Map<String, Integer> degreeMap = new HashMap<>(stateMap.size());
        for (State state : stateMap.values()) {
            degreeMap.put(state.getName(), state.getNbChild());
        }

        return degreeMap;
    }
}
