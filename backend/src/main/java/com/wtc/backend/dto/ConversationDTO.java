package com.wtc.backend.dto;

import java.time.Instant;

public class ConversationDTO {
    private String id, clientId, operatorId, lastMessage;
    private ClientDTO client;
    private Instant lastMessageAt, createdAt;
    private int unreadCount;
    private boolean isGroup;

    public ConversationDTO() {}
    private ConversationDTO(Builder b) {
        this.id = b.id; this.clientId = b.clientId; this.operatorId = b.operatorId;
        this.client = b.client; this.lastMessage = b.lastMessage;
        this.lastMessageAt = b.lastMessageAt; this.unreadCount = b.unreadCount;
        this.isGroup = b.isGroup; this.createdAt = b.createdAt;
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id, clientId, operatorId, lastMessage;
        private ClientDTO client;
        private Instant lastMessageAt, createdAt;
        private int unreadCount; private boolean isGroup;
        public Builder id(String v) { this.id = v; return this; }
        public Builder clientId(String v) { this.clientId = v; return this; }
        public Builder operatorId(String v) { this.operatorId = v; return this; }
        public Builder client(ClientDTO v) { this.client = v; return this; }
        public Builder lastMessage(String v) { this.lastMessage = v; return this; }
        public Builder lastMessageAt(Instant v) { this.lastMessageAt = v; return this; }
        public Builder unreadCount(int v) { this.unreadCount = v; return this; }
        public Builder isGroup(boolean v) { this.isGroup = v; return this; }
        public Builder createdAt(Instant v) { this.createdAt = v; return this; }
        public ConversationDTO build() { return new ConversationDTO(this); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    public ClientDTO getClient() { return client; }
    public void setClient(ClientDTO client) { this.client = client; }
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
}
