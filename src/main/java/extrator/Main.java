package extrator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Uso: java extrator.Main <arquivo> [--tudo] [--json]");
            return;
        }

        Path caminho = Path.of(args[0]);

        if (!Files.exists(caminho)) {
            System.err.println("Erro: arquivo não encontrado: " + caminho);
            return;
        }

        List<String> opcoes = List.of(args).subList(1, args.length);
        boolean tudo = opcoes.contains("--tudo");
        boolean json = opcoes.contains("--json");

        String nome = caminho.getFileName().toString();
        String extensao = nome.contains(".")
                ? nome.substring(nome.lastIndexOf('.') + 1).toLowerCase()
                : "";

        Relatorio relatorio = new Relatorio();
        relatorio.adicionar("Arquivo", "Nome", nome);
        relatorio.adicionar("Arquivo", "Tamanho", Files.size(caminho) + " bytes");

        switch (extensao) {
            case "jpg", "jpeg", "png", "heic", "heif", "tif", "tiff", "webp" ->
                    LeitorImagem.ler(caminho, tudo, relatorio);
            case "pdf" -> LeitorPdf.ler(caminho, relatorio);
            case "docx" -> LeitorDocx.ler(caminho, relatorio);
            default -> {
                System.err.println("Formato não suportado: ." + extensao);
                return;
            }
        }

        if (json) {
            Saida.json(relatorio);
        } else {
            Saida.texto(relatorio);
        }
    }
}