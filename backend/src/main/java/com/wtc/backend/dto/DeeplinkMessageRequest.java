package com.wtc.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class DeeplinkMessageRequest {
    @NotBlank(message = "senderId é obrigatório")
    private String senderId;
    @NotBlank(message = "senderType é obrigatório")
    private String senderType;
    private String content;
    @NotBlank(message = "deeplinkUrl é obrigatório")
    private String deeplinkUrl;
    @NotBlank(message = "deeplinkLabel é obrigatório")
    private String deeplinkLabel;

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getDeeplinkUrl() { return deeplinkUrl; }
    public void setDeeplinkUrl(String deeplinkUrl) { this.deeplinkUrl = deeplinkUrl; }
    public String getDeeplinkLabel() { return deeplinkLabel; }
    public void setDeeplinkLabel(String deeplinkLabel) { this.deeplinkLabel = deeplinkLabel; }
}
