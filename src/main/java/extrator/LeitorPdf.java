package extrator;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.nio.file.Path;

public class LeitorPdf {

    private static String t(String chave) {
        return Mensagens.get(chave);
    }

    public static void ler(Path caminho, Relatorio relatorio) throws Exception {
        try (PDDocument documento = Loader.loadPDF(caminho.toFile())) {
            PDDocumentInformation info = documento.getDocumentInformation();
            String secao = t("pdf.section");

            relatorio.adicionar(secao, t("pdf.title"), info.getTitle());
            relatorio.adicionar(secao, t("pdf.author"), info.getAuthor());
            relatorio.adicionar(secao, t("pdf.subject"), info.getSubject());
            relatorio.adicionar(secao, t("pdf.keywords"), info.getKeywords());
            relatorio.adicionar(secao, t("pdf.createdWith"), info.getCreator());
            relatorio.adicionar(secao, t("pdf.producer"), info.getProducer());
            relatorio.adicionar(secao, t("pdf.created"), Saida.data(info.getCreationDate()));
            relatorio.adicionar(secao, t("pdf.modified"), Saida.data(info.getModificationDate()));
            relatorio.adicionar(secao, t("pdf.pages"), String.valueOf(documento.getNumberOfPages()));
            relatorio.adicionar(secao, t("pdf.encrypted"),
                    documento.isEncrypted() ? t("common.yes") : t("common.no"));
        }
    }
}