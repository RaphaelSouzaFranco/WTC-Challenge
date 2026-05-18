package com.wtc.backend.dto;

import java.time.Instant;
import java.util.List;

public class ClientDTO {
    private String id, nome, numero, ramo, status, operatorId;
    private List<String> tags;
    private int score;
    private Instant createdAt, updatedAt;

    public ClientDTO() {}
    private ClientDTO(Builder b) {
        this.id = b.id; this.nome = b.nome; this.numero = b.numero; this.ramo = b.ramo;
        this.status = b.status; this.tags = b.tags; this.score = b.score;
        this.operatorId = b.operatorId; this.createdAt = b.createdAt; this.updatedAt = b.updatedAt;
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id, nome, numero, ramo, status, operatorId;
        private List<String> tags; private int score;
        private Instant createdAt, updatedAt;
        public Builder id(String v) { this.id = v; return this; }
        public Builder nome(String v) { this.nome = v; return this; }
        public Builder numero(String v) { this.numero = v; return this; }
        public Builder ramo(String v) { this.ramo = v; return this; }
        public Builder status(String v) { this.status = v; return this; }
        public Builder tags(List<String> v) { this.tags = v; return this; }
        public Builder score(int v) { this.score = v; return this; }
        public Builder operatorId(String v) { this.operatorId = v; return this; }
        public Builder createdAt(Instant v) { this.createdAt = v; return this; }
        public Builder updatedAt(Instant v) { this.updatedAt = v; return this; }
        public ClientDTO build() { return new ClientDTO(this); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getRamo() { return ramo; }
    public void setRamo(String ramo) { this.ramo = ramo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
