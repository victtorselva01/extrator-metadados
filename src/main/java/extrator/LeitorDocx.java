package extrator;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LeitorDocx {

    public static void ler(Path caminho) throws Exception {
        try (ZipFile zip = new ZipFile(caminho.toFile())) {
            Document core = abrir(zip, "docProps/core.xml");
            Document app = abrir(zip, "docProps/app.xml");

            System.out.println();
            System.out.println("=== Documento Word ===");
            boolean achou = false;
            if (core != null) {
                achou |= Saida.imprimir("Título", texto(core, "dc:title"));
                achou |= Saida.imprimir("Autor", texto(core, "dc:creator"));
                achou |= Saida.imprimir("Última modificação por", texto(core, "cp:lastModifiedBy"));
                achou |= Saida.imprimir("Assunto", texto(core, "dc:subject"));
                achou |= Saida.imprimir("Palavras-chave", texto(core, "cp:keywords"));
                achou |= Saida.imprimir("Criação", texto(core, "dcterms:created"));
                achou |= Saida.imprimir("Modificação", texto(core, "dcterms:modified"));
                achou |= Saida.imprimir("Revisão", texto(core, "cp:revision"));
            }
            if (app != null) {
                achou |= Saida.imprimir("Programa", texto(app, "Application"));
                achou |= Saida.imprimir("Versão do programa", texto(app, "AppVersion"));
                achou |= Saida.imprimir("Empresa", texto(app, "Company"));
            }
            if (!achou) {
                Saida.semDados();
            }
        }
    }

    private static Document abrir(ZipFile zip, String nome) throws Exception {
        ZipEntry entrada = zip.getEntry(nome);
        if (entrada == null) {
            return null;
        }
        DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
        // Segurança: bloqueia DOCTYPE para evitar ataques XXE em arquivos maliciosos
        fabrica.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        try (InputStream entradaXml = zip.getInputStream(entrada)) {
            return fabrica.newDocumentBuilder().parse(entradaXml);
        }
    }

    private static String texto(Document documento, String etiqueta) {
        NodeList nos = documento.getElementsByTagName(etiqueta);
        return nos.getLength() > 0 ? nos.item(0).getTextContent() : null;
    }
}