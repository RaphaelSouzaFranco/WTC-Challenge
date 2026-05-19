package com.wtc.backend.dto;

import java.time.Instant;

public class TimelineEventDTO {
    private String id;
    private String type;
    private String description;
    private Object data;
    private Instant timestamp;

    public TimelineEventDTO() {}
    private TimelineEventDTO(Builder b) {
        this.id = b.id; this.type = b.type; this.description = b.description;
        this.data = b.data; this.timestamp = b.timestamp;
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id, type, description;
        private Object data;
        private Instant timestamp;
        public Builder id(String v) { this.id = v; return this; }
        public Builder type(String v) { this.type = v; return this; }
        public Builder description(String v) { this.description = v; return this; }
        public Builder data(Object v) { this.data = v; return this; }
        public Builder timestamp(Instant v) { this.timestamp = v; return this; }
        public TimelineEventDTO build() { return new TimelineEventDTO(this); }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
