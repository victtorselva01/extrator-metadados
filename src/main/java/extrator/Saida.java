package extrator;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class Saida {

    // Imprime "rótulo: valor" só se houver valor. Retorna true se imprimiu.
    public static boolean imprimir(String rotulo, String valor) {
        if (valor == null || valor.isBlank()) {
            return false;
        }
        System.out.println(rotulo + ": " + valor);
        return true;
    }

    public static String data(Calendar calendario) {
        if (calendario == null) {
            return null;
        }
        ZonedDateTime data = calendario.toInstant()
                .atZone(calendario.getTimeZone().toZoneId());
        return data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));
    }

    public static void semDados() {
        System.out.println("Nenhum dado encontrado.");
    }
}