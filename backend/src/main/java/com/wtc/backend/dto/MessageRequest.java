package com.wtc.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class MessageRequest {
    @NotBlank(message = "senderId é obrigatório")
    private String senderId;
    @NotBlank(message = "senderType é obrigatório")
    private String senderType;
    private String content;
    private String type = "TEXT";

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
