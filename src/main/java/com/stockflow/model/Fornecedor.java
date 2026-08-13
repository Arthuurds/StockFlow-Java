package com.stockflow.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fornecedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String nomeJuridico;

    @Column(nullable = false, unique = true,length = 20)
    private String cnpj;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String telefone;


    // Construtor sem ID (para novos cadastros)
    public Fornecedor(String nome, String cnpj, String email, String telefone) {
        this.nomeJuridico = nome;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
    }

}
