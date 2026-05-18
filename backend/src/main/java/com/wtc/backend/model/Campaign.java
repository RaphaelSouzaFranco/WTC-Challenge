package com.wtc.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@Document(collection = "campaigns")
public class Campaign {

    @Id
    private String id;
    private String titulo;
    private String mensagem;
    private String targetAudience;
    private String mediaUrl;
    private String status;
    @Indexed
    private String operatorId;
    private Instant sentAt;
    @CreatedDate
    private Instant createdAt;

    public Campaign() {}

    private Campaign(Builder b) {
        this.titulo = b.titulo; this.mensagem = b.mensagem;
        this.targetAudience = b.targetAudience; this.status = b.status;
        this.operatorId = b.operatorId;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String titulo, mensagem, targetAudience, status, operatorId;
        public Builder titulo(String v) { this.titulo = v; return this; }
        public Builder mensagem(String v) { this.mensagem = v; return this; }
        public Builder targetAudience(String v) { this.targetAudience = v; return this; }
        public Builder status(String v) { this.status = v; return this; }
        public Builder operatorId(String v) { this.operatorId = v; return this; }
        public Campaign build() { return new Campaign(this); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public String getTargetAudience() { return targetAudience; }
    public void setTargetAudience(String targetAudience) { this.targetAudience = targetAudience; }
    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
