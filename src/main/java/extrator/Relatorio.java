package extrator;

import java.util.LinkedHashMap;
import java.util.Map;

public class Relatorio {
    private final Map<String, Map<String, String>> secoes = new LinkedHashMap<>();

    public void criarSecao(String secao) {
        secoes.computeIfAbsent(secao, nome -> new LinkedHashMap<>());
    }

    // Só guarda o campo se houver valor; a seção é criada de qualquer forma.
    public void adicionar(String secao, String campo, String valor) {
        criarSecao(secao);
        if (valor != null && !valor.isBlank()) {
            secoes.get(secao).put(campo, valor);
        }
    }

    public Map<String, Map<String, String>> getSecoes() {
        return secoes;
    }
}