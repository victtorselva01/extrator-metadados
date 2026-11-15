package extrator;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.nio.file.Path;

public class LeitorPdf {

    public static void ler(Path caminho) throws Exception {
        try (PDDocument documento = Loader.loadPDF(caminho.toFile())) {
            PDDocumentInformation info = documento.getDocumentInformation();

            System.out.println();
            System.out.println("=== Documento PDF ===");
            boolean achou = false;
            achou |= Saida.imprimir("Título", info.getTitle());
            achou |= Saida.imprimir("Autor", info.getAuthor());
            achou |= Saida.imprimir("Assunto", info.getSubject());
            achou |= Saida.imprimir("Palavras-chave", info.getKeywords());
            achou |= Saida.imprimir("Criado com", info.getCreator());
            achou |= Saida.imprimir("Gerado por", info.getProducer());
            achou |= Saida.imprimir("Criação", Saida.data(info.getCreationDate()));
            achou |= Saida.imprimir("Modificação", Saida.data(info.getModificationDate()));
            Saida.imprimir("Páginas", String.valueOf(documento.getNumberOfPages()));
            Saida.imprimir("Criptografado", documento.isEncrypted() ? "sim" : "não");
            if (!achou) {
                Saida.semDados();
            }
        }
    }
}