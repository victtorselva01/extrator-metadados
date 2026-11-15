package extrator;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java extrator.Main <caminho-do-arquivo>");
            return;
        }
        System.out.println("Arquivo recebido: " + args[0]);
    }
}