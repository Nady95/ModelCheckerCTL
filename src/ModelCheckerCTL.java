import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ctl.CTLFormula;
import ctl.atom.Atom;
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

/**
 * Main class of the application "Model Checker CTL"
 *
 * The goal of this app is to...
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public class ModelCheckerCTL {
    private static final KripkeStructure kripkeStructure = new KripkeStructure();
    private CTLFormula ctlFormula;

    public static void main(String[] args) {
        if(args.length == 0) {
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

            // Enfin, on récupère la formule CTL (debug car on ne fait que l'afficher pour le moment)
            parseFormulaFromJson(jsonObject.get("ctlformula").getAsString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createStateFromJson(JsonArray jsonArray) {
        for(JsonElement state : jsonArray) {

            // On récupère le nom de l'état
            String name = state.getAsJsonObject().get("name").getAsString();

            // On vérifie si c'est un état initial ou pas
            boolean isInitial = state.getAsJsonObject().get("isInitial").getAsBoolean();

            // On récupère les labels
            List<CTLFormula> labels = new ArrayList<>();
            JsonArray jArray = state.getAsJsonObject().getAsJsonArray("labels");
            for(int i=0; i<jArray.size();i++) {
                CTLFormula formula = new Atom(jArray.get(i).getAsJsonObject().get("atom").getAsString());
                labels.add(formula);
            }

            // Enfin, on crée l'état et on l'ajoute dans la structure de Kripke
            kripkeStructure.addStates(new State(name, isInitial, labels));
        }
    }

    public static void createTransitionsFromJson(JsonArray jsonArray) {
        for(JsonElement transition : jsonArray) {
            String stateAName = transition.getAsJsonObject().get("stateA").getAsString();
            String stateBName = transition.getAsJsonObject().get("stateB").getAsString();

            kripkeStructure.addTransition(
                    kripkeStructure.getStateFromName(stateAName),
                    kripkeStructure.getStateFromName(stateBName)
            );
        }
    }

    public static void parseFormulaFromJson(String formulaAsString) {
        //if()
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
