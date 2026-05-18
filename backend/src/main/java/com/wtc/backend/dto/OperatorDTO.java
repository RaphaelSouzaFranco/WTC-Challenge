package com.wtc.backend.dto;

public class OperatorDTO {
    private String id, nome, email, cargo, avatarUrl, notas;
    private boolean darkMode;

    public OperatorDTO() {}

    private OperatorDTO(Builder b) {
        this.id = b.id; this.nome = b.nome; this.email = b.email; this.cargo = b.cargo;
        this.avatarUrl = b.avatarUrl; this.notas = b.notas; this.darkMode = b.darkMode;
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id, nome, email, cargo, avatarUrl, notas;
        private boolean darkMode;
        public Builder id(String v) { this.id = v; return this; }
        public Builder nome(String v) { this.nome = v; return this; }
        public Builder email(String v) { this.email = v; return this; }
        public Builder cargo(String v) { this.cargo = v; return this; }
        public Builder avatarUrl(String v) { this.avatarUrl = v; return this; }
        public Builder notas(String v) { this.notas = v; return this; }
        public Builder darkMode(boolean v) { this.darkMode = v; return this; }
        public OperatorDTO build() { return new OperatorDTO(this); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }
}
