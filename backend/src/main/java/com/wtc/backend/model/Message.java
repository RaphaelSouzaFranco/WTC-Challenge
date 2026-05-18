package com.wtc.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@Document(collection = "messages")
public class Message {

    @Id
    private String id;
    @Indexed
    private String conversationId;
    private String senderId;
    private String senderType;
    private String type;
    private String content;
    private String mediaUrl;
    private String mediaType;
    private String deeplinkUrl;
    private String deeplinkLabel;
    private Instant readAt;
    @CreatedDate
    private Instant createdAt;

    public Message() {}

    private Message(Builder b) {
        this.conversationId = b.conversationId; this.senderId = b.senderId;
        this.senderType = b.senderType; this.type = b.type; this.content = b.content;
        this.mediaUrl = b.mediaUrl; this.mediaType = b.mediaType;
        this.deeplinkUrl = b.deeplinkUrl; this.deeplinkLabel = b.deeplinkLabel;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String conversationId, senderId, senderType, type, content;
        private String mediaUrl, mediaType, deeplinkUrl, deeplinkLabel;
        public Builder conversationId(String v) { this.conversationId = v; return this; }
        public Builder senderId(String v) { this.senderId = v; return this; }
        public Builder senderType(String v) { this.senderType = v; return this; }
        public Builder type(String v) { this.type = v; return this; }
        public Builder content(String v) { this.content = v; return this; }
        public Builder mediaUrl(String v) { this.mediaUrl = v; return this; }
        public Builder mediaType(String v) { this.mediaType = v; return this; }
        public Builder deeplinkUrl(String v) { this.deeplinkUrl = v; return this; }
        public Builder deeplinkLabel(String v) { this.deeplinkLabel = v; return this; }
        public Message build() { return new Message(this); }
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
