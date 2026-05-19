package com.wtc.backend.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class SegmentDTO {
    private String id, nome, descricao, operatorId;
    private Map<String, Object> criterios;
    private List<String> clientIds;
    private int clientCount;
    private Instant createdAt, updatedAt;

    public SegmentDTO() {}
    private SegmentDTO(Builder b) {
        this.id = b.id; this.nome = b.nome; this.descricao = b.descricao;
        this.operatorId = b.operatorId; this.criterios = b.criterios;
        this.clientIds = b.clientIds; this.clientCount = b.clientCount;
        this.createdAt = b.createdAt; this.updatedAt = b.updatedAt;
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id, nome, descricao, operatorId;
        private Map<String, Object> criterios;
        private List<String> clientIds;
        private int clientCount;
        private Instant createdAt, updatedAt;
        public Builder id(String v) { this.id = v; return this; }
        public Builder nome(String v) { this.nome = v; return this; }
        public Builder descricao(String v) { this.descricao = v; return this; }
        public Builder operatorId(String v) { this.operatorId = v; return this; }
        public Builder criterios(Map<String, Object> v) { this.criterios = v; return this; }
        public Builder clientIds(List<String> v) { this.clientIds = v; return this; }
        public Builder clientCount(int v) { this.clientCount = v; return this; }
        public Builder createdAt(Instant v) { this.createdAt = v; return this; }
        public Builder updatedAt(Instant v) { this.updatedAt = v; return this; }
        public SegmentDTO build() { return new SegmentDTO(this); }
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
    public int getClientCount() { return clientCount; }
    public void setClientCount(int clientCount) { this.clientCount = clientCount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
