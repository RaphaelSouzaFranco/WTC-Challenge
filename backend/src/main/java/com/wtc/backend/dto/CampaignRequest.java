package com.wtc.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class CampaignRequest {
    @NotBlank(message = "Título é obrigatório")
    private String titulo;
    @NotBlank(message = "Mensagem é obrigatória")
    private String mensagem;
    private String targetAudience = "Simple";
    private String segmentId;
    @NotBlank(message = "operatorId é obrigatório")
    private String operatorId;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public String getTargetAudience() { return targetAudience; }
    public void setTargetAudience(String targetAudience) { this.targetAudience = targetAudience; }
    public String getSegmentId() { return segmentId; }
    public void setSegmentId(String segmentId) { this.segmentId = segmentId; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
}
