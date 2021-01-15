import java.util.regex.Pattern;

/**
 * Classe servant à conserver les expressions régulières des formules CTL sous forme d'objets de classe Pattern.
 *
 * @author Nady Saddik
 * @author Rémi PHYU THANT THAR
 * @version 1.0
 * @since January 2021
 */
public final class PatternList {
    public static final Pattern ATOM = Pattern.compile("[a-z][a-z0-9_]*");
    public static final Pattern FALSE = Pattern.compile("false");
    public static final Pattern TRUE = Pattern.compile("true");

    public static final Pattern AND = Pattern.compile("(.+)(&&)(.+)");
    public static final Pattern NOT = Pattern.compile("(not)\\((.+)\\)");
    public static final Pattern OR = Pattern.compile("(.+)(\\|\\|)(.+)");

    public static final Pattern AF = Pattern.compile("(AF\\((.+)\\))");
    public static final Pattern AG = Pattern.compile("(AG\\((.+)\\))");
    public static final Pattern AU = Pattern.compile("(A(\\((.+)U(.+)\\)))");
    public static final Pattern AX = Pattern.compile("(AX\\((.+)\\))");

    public static final Pattern EF = Pattern.compile("(EF\\((.+)\\))");
    public static final Pattern EG = Pattern.compile("(EG\\((.+)\\))");
    public static final Pattern EU = Pattern.compile("(E(\\((.+)U(.+)\\)))");
    public static final Pattern EX = Pattern.compile("(EX\\((.+)\\))");

    /**
     * Constructeur interdisant l'instanciation d'un objet PatternList
     */
    private PatternList() {
        throw new AssertionError();
    }
}
