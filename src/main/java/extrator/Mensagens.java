package extrator;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Mensagens {

    private static final List<String> IDIOMAS = List.of("en", "pt", "es");

    private static ResourceBundle bundle = carregar("en");

    private static ResourceBundle carregar(String codigo) {
        return ResourceBundle.getBundle("mensagens", Locale.forLanguageTag(codigo));
    }

    // Retorna false se o idioma não for um dos suportados.
    public static boolean definirIdioma(String codigo) {
        if (!IDIOMAS.contains(codigo)) {
            return false;
        }
        bundle = carregar(codigo);
        return true;
    }

    public static String get(String chave) {
        return bundle.getString(chave);
    }

    public static String get(String chave, Object... argumentos) {
        return String.format(bundle.getString(chave), argumentos);
    }
}