package extrator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MensagensTest {

    @AfterEach
    void voltarParaIngles() {
        Mensagens.definirIdioma("en");
    }

    private ResourceBundle bundle(String codigo) {
        return ResourceBundle.getBundle("mensagens", Locale.forLanguageTag(codigo));
    }

    @Test
    void todosOsIdiomasTemExatamenteAsMesmasChaves() {
        assertEquals(bundle("en").keySet(), bundle("pt").keySet());
        assertEquals(bundle("en").keySet(), bundle("es").keySet());
    }

    @Test
    void trocarDeIdiomaMudaOsTextos() {
        Mensagens.definirIdioma("en");
        String ingles = Mensagens.get("device.section");

        Mensagens.definirIdioma("es");
        String espanhol = Mensagens.get("file.section");

        assertEquals("Device", ingles);
        assertEquals("Archivo", espanhol);
        assertNotEquals(ingles, espanhol);
    }

    @Test
    void rejeitaIdiomaNaoSuportado() {
        assertFalse(Mensagens.definirIdioma("fr"));
        assertTrue(Mensagens.definirIdioma("pt"));
    }

    @Test
    void mensagensComArgumentosSaoPreenchidas() {
        Mensagens.definirIdioma("pt");

        assertEquals("Erro: arquivo não encontrado: foto.jpg", Mensagens.get("error.notFound", "foto.jpg"));
    }
}