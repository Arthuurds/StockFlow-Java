package com.stockflow.model;


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


    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeJuridico() {
        return nomeJuridico;
    }

    public void setNomeJuridico(String nome) {
        this.nomeJuridico = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return String.format("Fornecedor [ID: %d | Nome: %s | CNPJ: %s | E-mail: %s | Tel: %s]",
                id, nomeJuridico, cnpj, email, telefone);
    }
}
