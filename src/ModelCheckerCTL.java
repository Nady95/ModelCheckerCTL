import kripke.KripkeStructure;

import java.io.*;

/**
 * Main class of the application "Model Checker CTL"
 *
 * The goal of this app is to...
 *
 * @author Nady Saddik
 * @version 1.0
 * @since January 2021
 */
public class ModelCheckerCTL {

    public static void main(String[] args) {
        if(args.length == 0) {
            throw new IllegalArgumentException("Il faut donner en paramètre un lien vers un fichier contenant " +
                    "une description textuelle d'une structure de Kripke (KS) et une formule CTL");
        }

        KripkeStructure kripkeStructure = new KripkeStructure();

    }

    public void parseFile(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            parseLine(line);
        }
    }

    public void parseLine(String line) {
        /*if () {

        }*/
    }
}
