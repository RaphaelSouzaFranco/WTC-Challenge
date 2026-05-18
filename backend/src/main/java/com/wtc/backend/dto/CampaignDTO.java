package com.wtc.backend.dto;

import java.time.Instant;

public class CampaignDTO {
    private String id, titulo, mensagem, targetAudience, mediaUrl, status, operatorId;
    private Instant sentAt, createdAt;

    public CampaignDTO() {}
    private CampaignDTO(Builder b) {
        this.id = b.id; this.titulo = b.titulo; this.mensagem = b.mensagem;
        this.targetAudience = b.targetAudience; this.mediaUrl = b.mediaUrl;
        this.status = b.status; this.operatorId = b.operatorId;
        this.sentAt = b.sentAt; this.createdAt = b.createdAt;
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id, titulo, mensagem, targetAudience, mediaUrl, status, operatorId;
        private Instant sentAt, createdAt;
        public Builder id(String v) { this.id = v; return this; }
        public Builder titulo(String v) { this.titulo = v; return this; }
        public Builder mensagem(String v) { this.mensagem = v; return this; }
        public Builder targetAudience(String v) { this.targetAudience = v; return this; }
        public Builder mediaUrl(String v) { this.mediaUrl = v; return this; }
        public Builder status(String v) { this.status = v; return this; }
        public Builder operatorId(String v) { this.operatorId = v; return this; }
        public Builder sentAt(Instant v) { this.sentAt = v; return this; }
        public Builder createdAt(Instant v) { this.createdAt = v; return this; }
        public CampaignDTO build() { return new CampaignDTO(this); }
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
