package util;

public class Console {

    public static void limpar() {
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

    public static void exibirSucesso(String mensagem) {
        System.out.println("\n[SUCESSO] " + mensagem);
    }

    public static void exibirErro(String mensagem) {
        System.out.println("\n[ERRO] " + mensagem);
    }

    public static void exibirCabecalho(String titulo) {
        System.out.println("\n============================================");
        System.out.println("    STOCKFLOW - " + titulo.toUpperCase());
        System.out.println("============================================");
    }
}
