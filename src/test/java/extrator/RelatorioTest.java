package extrator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RelatorioTest {

    @Test
    void naoGuardaCampoVazioMasCriaASecao() {
        Relatorio relatorio = new Relatorio();

        relatorio.adicionar("Dispositivo", "Modelo", null);
        relatorio.adicionar("Dispositivo", "Fabricante", "   ");

        assertTrue(relatorio.getSecoes().containsKey("Dispositivo"));
        assertTrue(relatorio.getSecoes().get("Dispositivo").isEmpty());
    }

    @Test
    void guardaCampoComValor() {
        Relatorio relatorio = new Relatorio();

        relatorio.adicionar("Dispositivo", "Modelo", "iPhone 11");

        assertEquals("iPhone 11", relatorio.getSecoes().get("Dispositivo").get("Modelo"));
    }

    @Test
    void mantemAOrdemDasSecoes() {
        Relatorio relatorio = new Relatorio();

        relatorio.criarSecao("B");
        relatorio.criarSecao("A");

        assertEquals(List.of("B", "A"), new ArrayList<>(relatorio.getSecoes().keySet()));
    }
}