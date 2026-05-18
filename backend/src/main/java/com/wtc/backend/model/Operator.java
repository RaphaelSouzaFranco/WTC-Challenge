package com.wtc.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@Document(collection = "operators")
public class Operator {

    @Id
    private String id;
    private String nome;
    @Indexed(unique = true)
    private String email;
    private String senha;
    private String cargo;
    private String avatarUrl;
    private boolean darkMode;
    private String notas;
    @CreatedDate
    private Instant createdAt;

    public Operator() {}

    private Operator(Builder b) {
        this.nome = b.nome; this.email = b.email; this.senha = b.senha;
        this.cargo = b.cargo; this.darkMode = b.darkMode; this.notas = b.notas;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String nome, email, senha, cargo, notas;
        private boolean darkMode;
        public Builder nome(String v) { this.nome = v; return this; }
        public Builder email(String v) { this.email = v; return this; }
        public Builder senha(String v) { this.senha = v; return this; }
        public Builder cargo(String v) { this.cargo = v; return this; }
        public Builder darkMode(boolean v) { this.darkMode = v; return this; }
        public Builder notas(String v) { this.notas = v; return this; }
        public Operator build() { return new Operator(this); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
