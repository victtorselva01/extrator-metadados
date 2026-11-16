package extrator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LeitorDocxTest {

    @TempDir
    Path pasta;

    private static final String NAMESPACES =
            "xmlns:cp=\"http://schemas.openxmlformats.org/package/2006/metadata/core-properties\" "
            + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\"";

    // Monta um .docx falso: um zip com os dois arquivos XML de metadados.
    private Path criarDocx(String core, String app) throws Exception {
        Path arquivo = pasta.resolve("teste.docx");
        try (ZipOutputStream zip = new ZipOutputStream(Files.newOutputStream(arquivo))) {
            zip.putNextEntry(new ZipEntry("docProps/core.xml"));
            zip.write(core.getBytes(StandardCharsets.UTF_8));
            zip.closeEntry();
            zip.putNextEntry(new ZipEntry("docProps/app.xml"));
            zip.write(app.getBytes(StandardCharsets.UTF_8));
            zip.closeEntry();
        }
        return arquivo;
    }

    @Test
    void leAutorEProgramaDoDocumento() throws Exception {
        String core = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<cp:coreProperties " + NAMESPACES + ">"
                + "<dc:creator>Maria Souza</dc:creator>"
                + "</cp:coreProperties>";
        String app = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<Properties><Application>Microsoft Office Word</Application></Properties>";
        Relatorio relatorio = new Relatorio();

        LeitorDocx.ler(criarDocx(core, app), relatorio);

        assertEquals("Maria Souza", relatorio.getSecoes().get("Documento Word").get("Autor"));
        assertEquals("Microsoft Office Word", relatorio.getSecoes().get("Documento Word").get("Programa"));
    }

    @Test
    void rejeitaXmlComDoctypeParaEvitarXxe() throws Exception {
        String core = "<?xml version=\"1.0\"?>"
                + "<!DOCTYPE x [<!ENTITY e SYSTEM \"file:///etc/passwd\">]>"
                + "<cp:coreProperties " + NAMESPACES + ">"
                + "<dc:creator>&e;</dc:creator>"
                + "</cp:coreProperties>";
        String app = "<?xml version=\"1.0\"?><Properties/>";
        Path docx = criarDocx(core, app);

        assertThrows(Exception.class, () -> LeitorDocx.ler(docx, new Relatorio()));
    }
}