package Util;

public class ValidarDocumento {

    /**
     * Valida matematicamente se um CPF é verdadeiro através dos seus dois dígitos verificadores.
     */
    public static boolean isCpfValido(String cpf) {
        if (cpf == null) return false;

        // Remove tudo o que não for número (pontos, traços e espaços)
        cpf = cpf.replaceAll("\\D", "");

        // CPF precisa ter 11 dígitos e não pode ser uma sequência idêntica (ex: 11111111111)
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            // Cálculo do 1º Dígito Verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int resto = 11 - (soma % 11);
            int digito1 = (resto == 10 || resto == 11) ? 0 : resto;

            // Cálculo do 2º Dígito Verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            resto = 11 - (soma % 11);
            int digito2 = (resto == 10 || resto == 11) ? 0 : resto;

            // Compara os dígitos calculados com os informados no CPF
            return digito1 == Character.getNumericValue(cpf.charAt(9)) &&
                    digito2 == Character.getNumericValue(cpf.charAt(10));

        } catch (Exception e) {
            return false;
        }
    }
}