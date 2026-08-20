package com.stockflow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String login;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @Column(nullable = false, length = 20)
    private String perfil; // Ex: "ADMIN", "OPERADOR"

    // Construtor padrão exigido pelo JPA
    public Usuario() {
    }

    // Construtor sem ID (para novos cadastros)
    public Usuario(String nome, String login, String senha, String perfil) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    // --- MÉTODOS OBRIGATÓRIOS DO UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Converte o seu atributo perfil em uma permissão que o Spring entende
        if ("ADMIN".equalsIgnoreCase(this.perfil)) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_OPERADOR")
            );
        }
        return List.of(new SimpleGrantedAuthority("ROLE_OPERADOR"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.login; // O Spring usará a sua variável 'login' para autenticar
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Getters e Setters dos seus atributos normais (id, nome, login, senha, perfil)
}
