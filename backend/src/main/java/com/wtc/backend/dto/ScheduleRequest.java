package com.wtc.backend.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class ScheduleRequest {
    @NotNull(message = "scheduledAt é obrigatório")
    private Instant scheduledAt;

    public Instant getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(Instant scheduledAt) { this.scheduledAt = scheduledAt; }
}
