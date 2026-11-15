package extrator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;

public class Saida {

    public static void texto(Relatorio relatorio) {
        boolean primeira = true;
        for (Map.Entry<String, Map<String, String>> secao : relatorio.getSecoes().entrySet()) {
            if (!primeira) {
                System.out.println();
            }
            primeira = false;

            System.out.println("=== " + secao.getKey() + " ===");
            if (secao.getValue().isEmpty()) {
                System.out.println("Nenhum dado encontrado.");
            } else {
                for (Map.Entry<String, String> campo : secao.getValue().entrySet()) {
                    System.out.println(campo.getKey() + ": " + campo.getValue());
                }
            }
        }
    }

    public static void json(Relatorio relatorio) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        System.out.println(gson.toJson(relatorio.getSecoes()));
    }

    public static String data(Calendar calendario) {
        if (calendario == null) {
            return null;
        }
        ZonedDateTime data = calendario.toInstant()
                .atZone(calendario.getTimeZone().toZoneId());
        return data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX"));
    }
}