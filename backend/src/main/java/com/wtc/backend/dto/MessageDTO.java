package com.wtc.backend.dto;

import java.time.Instant;

public class MessageDTO {
    private String id, conversationId, senderId, senderType, type, content;
    private String mediaUrl, mediaType, deeplinkUrl, deeplinkLabel;
    private Instant readAt, createdAt;

    public MessageDTO() {}
    private MessageDTO(Builder b) {
        this.id = b.id; this.conversationId = b.conversationId; this.senderId = b.senderId;
        this.senderType = b.senderType; this.type = b.type; this.content = b.content;
        this.mediaUrl = b.mediaUrl; this.mediaType = b.mediaType;
        this.deeplinkUrl = b.deeplinkUrl; this.deeplinkLabel = b.deeplinkLabel;
        this.readAt = b.readAt; this.createdAt = b.createdAt;
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id, conversationId, senderId, senderType, type, content;
        private String mediaUrl, mediaType, deeplinkUrl, deeplinkLabel;
        private Instant readAt, createdAt;
        public Builder id(String v) { this.id = v; return this; }
        public Builder conversationId(String v) { this.conversationId = v; return this; }
        public Builder senderId(String v) { this.senderId = v; return this; }
        public Builder senderType(String v) { this.senderType = v; return this; }
        public Builder type(String v) { this.type = v; return this; }
        public Builder content(String v) { this.content = v; return this; }
        public Builder mediaUrl(String v) { this.mediaUrl = v; return this; }
        public Builder mediaType(String v) { this.mediaType = v; return this; }
        public Builder deeplinkUrl(String v) { this.deeplinkUrl = v; return this; }
        public Builder deeplinkLabel(String v) { this.deeplinkLabel = v; return this; }
        public Builder readAt(Instant v) { this.readAt = v; return this; }
        public Builder createdAt(Instant v) { this.createdAt = v; return this; }
        public MessageDTO build() { return new MessageDTO(this); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getConversationId() { return conversationId; }
    public void setConversationId(String v) { this.conversationId = v; }
    public String getSenderId() { return senderId; }
    public void setSenderId(String v) { this.senderId = v; }
    public String getSenderType() { return senderType; }
    public void setSenderType(String v) { this.senderType = v; }
    public String getType() { return type; }
    public void setType(String v) { this.type = v; }
    public String getContent() { return content; }
    public void setContent(String v) { this.content = v; }
    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String v) { this.mediaUrl = v; }
    public String getMediaType() { return mediaType; }
    public void setMediaType(String v) { this.mediaType = v; }
    public String getDeeplinkUrl() { return deeplinkUrl; }
    public void setDeeplinkUrl(String v) { this.deeplinkUrl = v; }
    public String getDeeplinkLabel() { return deeplinkLabel; }
    public void setDeeplinkLabel(String v) { this.deeplinkLabel = v; }
    public Instant getReadAt() { return readAt; }
    public void setReadAt(Instant v) { this.readAt = v; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant v) { this.createdAt = v; }
}
