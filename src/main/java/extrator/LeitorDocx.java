package extrator;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LeitorDocx {

    private static String t(String chave) {
        return Mensagens.get(chave);
    }

    public static void ler(Path caminho, Relatorio relatorio) throws Exception {
        try (ZipFile zip = new ZipFile(caminho.toFile())) {
            Document core = abrir(zip, "docProps/core.xml");
            Document app = abrir(zip, "docProps/app.xml");
            String secao = t("docx.section");

            relatorio.criarSecao(secao);
            if (core != null) {
                relatorio.adicionar(secao, t("docx.title"), texto(core, "dc:title"));
                relatorio.adicionar(secao, t("docx.author"), texto(core, "dc:creator"));
                relatorio.adicionar(secao, t("docx.lastModifiedBy"), texto(core, "cp:lastModifiedBy"));
                relatorio.adicionar(secao, t("docx.subject"), texto(core, "dc:subject"));
                relatorio.adicionar(secao, t("docx.keywords"), texto(core, "cp:keywords"));
                relatorio.adicionar(secao, t("docx.created"), texto(core, "dcterms:created"));
                relatorio.adicionar(secao, t("docx.modified"), texto(core, "dcterms:modified"));
                relatorio.adicionar(secao, t("docx.revision"), texto(core, "cp:revision"));
            }
            if (app != null) {
                relatorio.adicionar(secao, t("docx.application"), texto(app, "Application"));
                relatorio.adicionar(secao, t("docx.appVersion"), texto(app, "AppVersion"));
                relatorio.adicionar(secao, t("docx.company"), texto(app, "Company"));
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