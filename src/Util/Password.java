package Util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Password {

    /**
     * Transforma uma senha em texto puro num hash SHA-256 de 64 caracteres.
     */
    public static String hashPassword(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha não pode ser vazia.");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar algoritmo de criptografia.", e);
        }
    }

    /**
     * Compara a senha digitada pelo usuário no login com o hash armazenado no banco.
     */
    public static boolean verificarSenha(String senhaDigitada, String hashBanco) {
        String hashDigitada = hashPassword(senhaDigitada);
        return hashDigitada.equalsIgnoreCase(hashBanco);
    }
}
