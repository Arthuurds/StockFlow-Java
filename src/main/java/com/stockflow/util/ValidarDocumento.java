package com.stockflow.util;

public final class ValidarDocumento {

    private ValidarDocumento() {
    }

    public static boolean isCpfValido(String cpf) {
        if (cpf == null) return false;

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int digito1 = calcularDigitoCpf(cpf, 9);
        int digito2 = calcularDigitoCpf(cpf, 10);

        return digito1 == Character.getNumericValue(cpf.charAt(9))
                && digito2 == Character.getNumericValue(cpf.charAt(10));
    }

    public static boolean isCnpjValido(String cnpj) {
        if (cnpj == null) return false;

        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int digito1 = calcularDigitoCnpj(cnpj, pesos1);
        int digito2 = calcularDigitoCnpj(cnpj, pesos2);

        return digito1 == Character.getNumericValue(cnpj.charAt(12))
                && digito2 == Character.getNumericValue(cnpj.charAt(13));
    }

    /** Aceita CPF (11 dígitos) ou CNPJ (14 dígitos), com ou sem pontuação. */
    public static boolean isCpfOuCnpjValido(String documento) {
        if (documento == null) return false;
        String numeros = documento.replaceAll("\\D", "");
        return numeros.length() == 11 ? isCpfValido(numeros) : isCnpjValido(numeros);
    }

    private static int calcularDigitoCpf(String cpf, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (quantidade + 1 - i);
        }
        int resto = 11 - (soma % 11);
        return (resto == 10 || resto == 11) ? 0 : resto;
    }

    private static int calcularDigitoCnpj(String cnpj, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
