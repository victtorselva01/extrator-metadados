package extrator;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SaidaTest {

    // Captura o que o código imprime no console, para poder conferir no teste.
    private String capturar(Runnable acao) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            acao.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private Relatorio relatorioDeExemplo() {
        Relatorio relatorio = new Relatorio();
        relatorio.adicionar("Arquivo", "Nome", "foto.jpg");
        relatorio.adicionar("Dispositivo", "Modelo", "iPhone 11");
        relatorio.adicionar("Localização", "Mapa", "https://maps.example/?q=1,2&z=3");
        relatorio.criarSecao("Vazia");
        return relatorio;
    }

    @Test
    void textoMostraCamposEAvisaQuandoSecaoEstaVazia() {
        String saida = capturar(() -> Saida.texto(relatorioDeExemplo()));

        assertTrue(saida.contains("=== Dispositivo ==="));
        assertTrue(saida.contains("Modelo: iPhone 11"));
        assertTrue(saida.contains(Mensagens.get("out.noData")));
    }

    @Test
    void jsonContemOsCamposEManteAOrdemDasSecoes() {
        String saida = capturar(() -> Saida.json(relatorioDeExemplo()));

        assertTrue(saida.contains("\"Modelo\": \"iPhone 11\""));
        assertTrue(saida.indexOf("\"Arquivo\"") < saida.indexOf("\"Dispositivo\""));
    }

    @Test
    void jsonNaoTrocaCaracteresDeLinksPorCodigos() {
        String saida = capturar(() -> Saida.json(relatorioDeExemplo()));

        assertTrue(saida.contains("?q=1,2&z=3"));
    }

    @Test
    void dataNulaRetornaNulo() {
        assertNull(Saida.data(null));
    }

    @Test
    void dataEmUtcSaiComZ() {
        Calendar calendario = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendario.set(2026, Calendar.NOVEMBER, 15, 9, 53, 11);

        assertEquals("2026-11-15 09:53:11 Z", Saida.data(calendario));
    }
}