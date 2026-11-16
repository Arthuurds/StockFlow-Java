package com.stockflow.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidarDocumentoTest {

    @ParameterizedTest
    @ValueSource(strings = {"529.982.247-25", "11144477735"})
    void deveAceitarCpfValido(String cpf) {
        assertTrue(ValidarDocumento.isCpfValido(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {"529.982.247-24", "111.111.111-11", "123"})
    void deveRecusarCpfInvalido(String cpf) {
        assertFalse(ValidarDocumento.isCpfValido(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {"11.222.333/0001-81", "45997418000153"})
    void deveAceitarCnpjValido(String cnpj) {
        assertTrue(ValidarDocumento.isCnpjValido(cnpj));
    }

    @ParameterizedTest
    @ValueSource(strings = {"11.222.333/0001-80", "00.000.000/0000-00", "123"})
    void deveRecusarCnpjInvalido(String cnpj) {
        assertFalse(ValidarDocumento.isCnpjValido(cnpj));
    }

    @Test
    void deveIdentificarCpfOuCnpjPeloTamanho() {
        assertTrue(ValidarDocumento.isCpfOuCnpjValido("529.982.247-25"));
        assertTrue(ValidarDocumento.isCpfOuCnpjValido("11.222.333/0001-81"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void deveRecusarDocumentoVazio(String documento) {
        assertFalse(ValidarDocumento.isCpfOuCnpjValido(documento));
    }
}
