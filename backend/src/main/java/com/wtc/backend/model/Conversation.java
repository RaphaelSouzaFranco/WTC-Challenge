package com.wtc.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@Document(collection = "conversations")
public class Conversation {

    @Id
    private String id;
    @Indexed
    private String clientId;
    @Indexed
    private String operatorId;
    private String lastMessage;
    private Instant lastMessageAt;
    private int unreadCount;
    private boolean isGroup;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

    public Conversation() {}

    private Conversation(Builder b) {
        this.clientId = b.clientId; this.operatorId = b.operatorId;
        this.lastMessage = b.lastMessage; this.lastMessageAt = b.lastMessageAt;
        this.unreadCount = b.unreadCount; this.isGroup = b.isGroup;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String clientId, operatorId, lastMessage;
        private Instant lastMessageAt;
        private int unreadCount;
        private boolean isGroup;
        public Builder clientId(String v) { this.clientId = v; return this; }
        public Builder operatorId(String v) { this.operatorId = v; return this; }
        public Builder lastMessage(String v) { this.lastMessage = v; return this; }
        public Builder lastMessageAt(Instant v) { this.lastMessageAt = v; return this; }
        public Builder unreadCount(int v) { this.unreadCount = v; return this; }
        public Builder isGroup(boolean v) { this.isGroup = v; return this; }
        public Conversation build() { return new Conversation(this); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
    public Instant getLastMessageAt() { return lastMessageAt; }
    public void setLastMessageAt(Instant lastMessageAt) { this.lastMessageAt = lastMessageAt; }
    public int getUnreadCount() { return unreadCount; }
    public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }
    public boolean isGroup() { return isGroup; }
    public void setGroup(boolean group) { isGroup = group; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
