package extrator;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.nio.file.Path;

public class LeitorPdf {

    public static void ler(Path caminho, Relatorio relatorio) throws Exception {
        try (PDDocument documento = Loader.loadPDF(caminho.toFile())) {
            PDDocumentInformation info = documento.getDocumentInformation();
            String secao = "Documento PDF";

            relatorio.adicionar(secao, "Título", info.getTitle());
            relatorio.adicionar(secao, "Autor", info.getAuthor());
            relatorio.adicionar(secao, "Assunto", info.getSubject());
            relatorio.adicionar(secao, "Palavras-chave", info.getKeywords());
            relatorio.adicionar(secao, "Criado com", info.getCreator());
            relatorio.adicionar(secao, "Gerado por", info.getProducer());
            relatorio.adicionar(secao, "Criação", Saida.data(info.getCreationDate()));
            relatorio.adicionar(secao, "Modificação", Saida.data(info.getModificationDate()));
            relatorio.adicionar(secao, "Páginas", String.valueOf(documento.getNumberOfPages()));
            relatorio.adicionar(secao, "Criptografado", documento.isEncrypted() ? "sim" : "não");
        }
    }
}