package extrator;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Uso: java extrator.Main <caminho-do-arquivo> [--tudo]");
            return;
        }

        Path caminho = Path.of(args[0]);

        if (!Files.exists(caminho)) {
            System.out.println("Erro: arquivo não encontrado: " + caminho);
            return;
        }

        String nome = caminho.getFileName().toString();
        String extensao = nome.contains(".")
                ? nome.substring(nome.lastIndexOf('.') + 1).toLowerCase()
                : "";
        boolean tudo = args.length > 1 && args[1].equals("--tudo");

        System.out.println("=== Arquivo ===");
        System.out.println("Nome: " + nome);
        System.out.println("Tamanho: " + Files.size(caminho) + " bytes");

        switch (extensao) {
            case "jpg", "jpeg", "png", "heic", "heif", "tif", "tiff", "webp" ->
                    LeitorImagem.ler(caminho, tudo);
            case "pdf" -> LeitorPdf.ler(caminho);
            case "docx" -> LeitorDocx.ler(caminho);
            default -> System.out.println("Formato não suportado: ." + extensao);
        }
    }
}