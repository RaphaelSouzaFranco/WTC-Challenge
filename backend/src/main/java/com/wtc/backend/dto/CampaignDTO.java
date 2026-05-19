package com.wtc.backend.dto;

import java.time.Instant;

public class CampaignDTO {
    private String id, titulo, mensagem, targetAudience, segmentId, mediaUrl, status, operatorId;
    private String variantOf, variantLabel;
    private Instant scheduledAt, sentAt, createdAt;

    public CampaignDTO() {}
    private CampaignDTO(Builder b) {
        this.id = b.id; this.titulo = b.titulo; this.mensagem = b.mensagem;
        this.targetAudience = b.targetAudience; this.segmentId = b.segmentId;
        this.mediaUrl = b.mediaUrl; this.status = b.status; this.operatorId = b.operatorId;
        this.variantOf = b.variantOf; this.variantLabel = b.variantLabel;
        this.scheduledAt = b.scheduledAt; this.sentAt = b.sentAt; this.createdAt = b.createdAt;
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id, titulo, mensagem, targetAudience, segmentId, mediaUrl, status, operatorId;
        private String variantOf, variantLabel;
        private Instant scheduledAt, sentAt, createdAt;
        public Builder id(String v) { this.id = v; return this; }
        public Builder titulo(String v) { this.titulo = v; return this; }
        public Builder mensagem(String v) { this.mensagem = v; return this; }
        public Builder targetAudience(String v) { this.targetAudience = v; return this; }
        public Builder segmentId(String v) { this.segmentId = v; return this; }
        public Builder mediaUrl(String v) { this.mediaUrl = v; return this; }
        public Builder status(String v) { this.status = v; return this; }
        public Builder operatorId(String v) { this.operatorId = v; return this; }
        public Builder variantOf(String v) { this.variantOf = v; return this; }
        public Builder variantLabel(String v) { this.variantLabel = v; return this; }
        public Builder scheduledAt(Instant v) { this.scheduledAt = v; return this; }
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
    public String getSegmentId() { return segmentId; }
    public void setSegmentId(String segmentId) { this.segmentId = segmentId; }
    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    public String getVariantOf() { return variantOf; }
    public void setVariantOf(String variantOf) { this.variantOf = variantOf; }
    public String getVariantLabel() { return variantLabel; }
    public void setVariantLabel(String variantLabel) { this.variantLabel = variantLabel; }
    public Instant getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(Instant scheduledAt) { this.scheduledAt = scheduledAt; }
    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
