package extrator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println(Mensagens.get("usage"));
            return;
        }

        List<String> opcoes = List.of(args).subList(1, args.length);

        int posicaoIdioma = opcoes.indexOf("--lang");
        if (posicaoIdioma >= 0) {
            String codigo = posicaoIdioma + 1 < opcoes.size() ? opcoes.get(posicaoIdioma + 1) : "";
            if (!Mensagens.definirIdioma(codigo)) {
                System.err.println(Mensagens.get("error.badLang", codigo));
                return;
            }
        }

        boolean tudo = opcoes.contains("--all") || opcoes.contains("--tudo");
        boolean json = opcoes.contains("--json");

        Path caminho = Path.of(args[0]);

        if (!Files.exists(caminho)) {
            System.err.println(Mensagens.get("error.notFound", caminho));
            return;
        }

        String nome = caminho.getFileName().toString();
        String extensao = nome.contains(".")
                ? nome.substring(nome.lastIndexOf('.') + 1).toLowerCase()
                : "";

        Relatorio relatorio = new Relatorio();
        String secaoArquivo = Mensagens.get("file.section");
        relatorio.adicionar(secaoArquivo, Mensagens.get("file.name"), nome);
        relatorio.adicionar(secaoArquivo, Mensagens.get("file.size"), Files.size(caminho) + " bytes");

        switch (extensao) {
            case "jpg", "jpeg", "png", "heic", "heif", "tif", "tiff", "webp" ->
                    LeitorImagem.ler(caminho, tudo, relatorio);
            case "pdf" -> LeitorPdf.ler(caminho, relatorio);
            case "docx" -> LeitorDocx.ler(caminho, relatorio);
            default -> {
                System.err.println(Mensagens.get("error.unsupported", extensao));
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