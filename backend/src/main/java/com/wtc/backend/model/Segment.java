package com.wtc.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Document(collection = "segments")
public class Segment {

    @Id
    private String id;
    private String nome;
    private String descricao;
    @Indexed
    private String operatorId;
    private Map<String, Object> criterios;
    private List<String> clientIds;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

    public Segment() {}

    private Segment(Builder b) {
        this.nome = b.nome; this.descricao = b.descricao;
        this.operatorId = b.operatorId; this.criterios = b.criterios;
        this.clientIds = b.clientIds;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String nome, descricao, operatorId;
        private Map<String, Object> criterios;
        private List<String> clientIds;
        public Builder nome(String v) { this.nome = v; return this; }
        public Builder descricao(String v) { this.descricao = v; return this; }
        public Builder operatorId(String v) { this.operatorId = v; return this; }
        public Builder criterios(Map<String, Object> v) { this.criterios = v; return this; }
        public Builder clientIds(List<String> v) { this.clientIds = v; return this; }
        public Segment build() { return new Segment(this); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    public Map<String, Object> getCriterios() { return criterios; }
    public void setCriterios(Map<String, Object> criterios) { this.criterios = criterios; }
    public List<String> getClientIds() { return clientIds; }
    public void setClientIds(List<String> clientIds) { this.clientIds = clientIds; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
