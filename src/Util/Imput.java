package Util;

import java.math.BigDecimal;
import java.util.Scanner;

public class Imput {

    private static final Scanner scanner = new Scanner(System.in);

    public static String lerString(String mensagem) {
        System.out.print(mensagem + ": ");
        return scanner.nextLine().trim();
    }

    public static int lerInt(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem + ": ");
                String valor = scanner.nextLine().trim();
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                Console.exibirErro("Entrada inválida! Digite um número inteiro.");
            }
        }
    }

    public static long lerLong(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem + ": ");
                String valor = scanner.nextLine().trim();
                return Long.parseLong(valor);
            } catch (NumberFormatException e) {
                Console.exibirErro("Entrada inválida! Digite um número inteiro válido.");
            }
        }
    }

    public static BigDecimal lerBigDecimal(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem + ": R$ ");
                String valor = scanner.nextLine().trim().replace(",", ".");
                BigDecimal decimal = new BigDecimal(valor);
                if (decimal.compareTo(BigDecimal.ZERO) < 0) {
                    Console.exibirErro("O valor não pode ser negativo.");
                    continue;
                }
                return decimal;
            } catch (Exception e) {
                Console.exibirErro("Valor decimal inválido! Exemplo de uso: 49.90");
            }
        }
    }
}
