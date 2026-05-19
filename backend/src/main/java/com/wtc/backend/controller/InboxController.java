package com.wtc.backend.controller;

import com.wtc.backend.dto.ConversationDTO;
import com.wtc.backend.dto.MessageDTO;
import com.wtc.backend.service.InboxService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inbox")
public class InboxController {

    private final InboxService inboxService;

    public InboxController(InboxService inboxService) {
        this.inboxService = inboxService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<ConversationDTO>> getInbox(@PathVariable String customerId) {
        return ResponseEntity.ok(inboxService.getConversationsForClient(customerId));
    }

    @GetMapping("/{customerId}/messages")
    public ResponseEntity<List<MessageDTO>> getMessages(
            @PathVariable String customerId,
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(inboxService.getMessagesForClient(customerId, limit));
    }
}
