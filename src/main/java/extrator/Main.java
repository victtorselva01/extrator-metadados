package extrator;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Uso: java extrator.Main <caminho-do-arquivo>");
            return;
        }

        Path caminho = Path.of(args[0]);

        if (!Files.exists(caminho)) {
            System.out.println("Erro: arquivo não encontrado: " + caminho);
            return;
        }

        String nome = caminho.getFileName().toString();
        long tamanho = Files.size(caminho);
        String extensao = nome.contains(".")
                ? nome.substring(nome.lastIndexOf('.') + 1).toLowerCase()
                : "(nenhuma)";

        System.out.println("Nome: " + nome);
        System.out.println("Tamanho: " + tamanho + " bytes");
        System.out.println("Extensão: " + extensao);
    }
}