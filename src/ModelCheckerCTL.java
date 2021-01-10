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
import java.util.regex.Pattern;

/**
 * Main class of the application "Model Checker CTL"
 * <p>
 * The goal of this app is to...
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class ModelCheckerCTL {
    private static final KripkeStructure kripkeStructure = new KripkeStructure();
    private static CTLFormula ctlFormula;

    private static final Pattern PATTERN_ATOM = Pattern.compile("[a-z][a-zA-Z0-9\\_]*");
    private static final Pattern PATTERN_FALSE = Pattern.compile("false");
    private static final Pattern PATTERN_TRUE = Pattern.compile("true");

    private static final Pattern PATTERN_AND = Pattern.compile("(.+)(&&)(.+)");
    private static final Pattern PATTERN_NOT = Pattern.compile("(not)\\((.+)\\)");
    private static final Pattern PATTERN_OR = Pattern.compile("(.+)(\\|\\|)(.+)");

    private static final Pattern PATTERN_AF = Pattern.compile("(AF\\((.+)\\))");
    private static final Pattern PATTERN_AG = Pattern.compile("(AG\\((.+)\\))");
    private static final Pattern PATTERN_AU = Pattern.compile("(A(\\((.+)U(.+)\\)))");
    private static final Pattern PATTERN_AX = Pattern.compile("(AX\\((.+)\\))");

    private static final Pattern PATTERN_EF = Pattern.compile("(EF\\((.+)\\))");
    private static final Pattern PATTERN_EG = Pattern.compile("(EG\\((.+)\\))");
    private static final Pattern PATTERN_EU = Pattern.compile("(E(\\((.+)U(.+)\\)))");
    private static final Pattern PATTERN_EX = Pattern.compile("(EX\\((.+)\\))");


    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Il faut donner en paramètre un lien vers un fichier contenant " +
                    "une description textuelle d'une structure de Kripke (KS) et une formule CTL");
        }

        readFile(args[0]);

        // DEBUG
        for (State s : kripkeStructure.getStates().values()) {
            System.out.println(s.getName() + "," + s.isInitial() + "," + s.getLabels() + "," + s.getLinkedStates());
        }

        marking(new Atom("b"));
    }

    public static void readFile(String filePath) {
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


    public static void createStateFromJson(JsonArray jsonArray) {
        for (JsonElement state : jsonArray) {

            // On récupère le nom de l'état
            String name = state.getAsJsonObject().get("name").getAsString();

            // On vérifie si c'est un état initial ou pas
            boolean isInitial = state.getAsJsonObject().get("isInitial").getAsBoolean();

            // On récupère les labels
            List<CTLFormula> labels = new ArrayList<>();
            JsonArray jArray = state.getAsJsonObject().getAsJsonArray("labels");
            for (int i = 0; i < jArray.size(); i++) {
                CTLFormula formula = new Atom(jArray.get(i).getAsJsonObject().get("atom").getAsString());
                labels.add(formula);
            }

            // Enfin, on crée l'état et on l'ajoute dans la structure de Kripke
            kripkeStructure.addStates(new State(name, isInitial, labels));
        }
    }

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

    public static CTLFormula parseFormulaFromJson(String formulaAsString) {
        // On crée nos matchers qui vont parcourir le texte en cherchant le pattern (peut être simplifié)
        Matcher MATCHER_ATOM = PATTERN_ATOM.matcher(formulaAsString);
        Matcher MATCHER_FALSE = PATTERN_FALSE.matcher(formulaAsString);
        Matcher MATCHER_TRUE = PATTERN_TRUE.matcher(formulaAsString);

        Matcher MATCHER_AND = PATTERN_AND.matcher(formulaAsString);
        Matcher MATCHER_NOT = PATTERN_NOT.matcher(formulaAsString);
        Matcher MATCHER_OR = PATTERN_OR.matcher(formulaAsString);

        Matcher MATCHER_AF = PATTERN_AF.matcher(formulaAsString);
        Matcher MATCHER_AG = PATTERN_AG.matcher(formulaAsString);
        Matcher MATCHER_AU = PATTERN_AU.matcher(formulaAsString);
        Matcher MATCHER_AX = PATTERN_AX.matcher(formulaAsString);

        Matcher MATCHER_EF = PATTERN_EF.matcher(formulaAsString);
        Matcher MATCHER_EG = PATTERN_EG.matcher(formulaAsString);
        Matcher MATCHER_EU = PATTERN_EU.matcher(formulaAsString);
        Matcher MATCHER_EX = PATTERN_EX.matcher(formulaAsString);

        if(MATCHER_AF.matches()) {
            System.out.println("MATCH_AF:" + MATCHER_AF.group());
            return new AF(parseFormulaFromJson(MATCHER_AF.group(2)));
        } else if (MATCHER_AG.matches()) {
            System.out.println("MATCH_AG:" + MATCHER_AG.group());
            return new AG(parseFormulaFromJson(MATCHER_AG.group(2)));
        } else if (MATCHER_AU.matches()) {
            System.out.println("MATCH_AU:" + MATCHER_AU.group());
            return new AU(parseFormulaFromJson(MATCHER_AU.group(3)), parseFormulaFromJson(MATCHER_AU.group(4)));
        } else if (MATCHER_AX.matches()) {
            System.out.println("MATCH_AX:" + MATCHER_AX.group());
            return new AX(parseFormulaFromJson(MATCHER_AX.group(2)));
        } else if (MATCHER_EF.matches()) {
            System.out.println("MATCH_EF:" + MATCHER_EF.group());
            return new EF(parseFormulaFromJson(MATCHER_EF.group(2)));
        } else if (MATCHER_EG.matches()) {
            System.out.println("MATCH_EG:" + MATCHER_EG.group());
            return new EG(parseFormulaFromJson(MATCHER_EG.group(2)));
        } else if (MATCHER_EU.matches()) {
            System.out.println("MATCH_EU:" + MATCHER_EU.group());
            return new EU(parseFormulaFromJson(MATCHER_EU.group(3)), parseFormulaFromJson(MATCHER_EU.group(4)));
        } else if (MATCHER_EX.matches()) {
            System.out.println("MATCH_EX:" + MATCHER_EX.group());
            return new EX(parseFormulaFromJson(MATCHER_EX.group(2)));
        } else if (MATCHER_NOT.matches()) {
            System.out.println("MATCH_NOT:" + MATCHER_NOT.group());
            return new Not(parseFormulaFromJson(MATCHER_NOT.group(2)));
        } else if (MATCHER_AND.matches()) {
            System.out.println("MATCH_AND:" + MATCHER_AND.group());
            return new And(parseFormulaFromJson(MATCHER_AND.group(1)), parseFormulaFromJson(MATCHER_AND.group(3)));
        } else if (MATCHER_OR.matches()) {
            System.out.println("MATCH_OR:" + MATCHER_OR.group());
            return new Or(parseFormulaFromJson(MATCHER_OR.group(1)), parseFormulaFromJson(MATCHER_OR.group(3)));
        } else if (MATCHER_TRUE.matches()) {
            System.out.println("MATCH_TRUE:" + MATCHER_TRUE.group());
            return new True();
        } else if (MATCHER_FALSE.matches()) {
            System.out.println("MATCH_FALSE:" + MATCHER_FALSE.group());
            return new False();
        } else if (MATCHER_ATOM.matches()) {
            System.out.println("MATCH_ATOM:" + MATCHER_ATOM.group());
            return new Atom(MATCHER_ATOM.group());
        } else {
            System.out.println("ERROR????");
            return new Atom("ERROR");
        }
    }

    public static Map<State, Boolean> marking(CTLFormula formula) {
        Map<State, Boolean> statesBool = new HashMap<>();
        for (State state : kripkeStructure.getStates().values()) {
            if (state.getLabels().contains(formula)) {
                statesBool.put(state, Boolean.TRUE);
            } else {
                statesBool.put(state, Boolean.FALSE);
            }
        }
        return statesBool;
    }
}
