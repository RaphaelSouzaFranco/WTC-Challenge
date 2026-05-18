package com.wtc.backend.service;

import com.wtc.backend.dto.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service de Server-Sent Events (SSE) para dados em tempo real.
 *
 * Canal 1: /api/messages/{conversationId}/stream — novas mensagens
 * Canal 2: /api/conversations/stream?operatorId={id} — atualizações de conversas
 */
@Service
public class SseService {

    private static final Logger log = LoggerFactory.getLogger(SseService.class);
    private static final long SSE_TIMEOUT = 30 * 60 * 1000L;

    private final Map<String, List<SseEmitter>> messageEmitters = new ConcurrentHashMap<>();
    private final Map<String, List<SseEmitter>> conversationEmitters = new ConcurrentHashMap<>();

    public SseEmitter createMessageEmitter(String conversationId) {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        List<SseEmitter> emitters = messageEmitters.computeIfAbsent(conversationId, k -> new CopyOnWriteArrayList<>());
        emitters.add(emitter);
        emitter.onCompletion(() -> removeMessageEmitter(conversationId, emitter));
        emitter.onTimeout(() -> removeMessageEmitter(conversationId, emitter));
        emitter.onError(e -> removeMessageEmitter(conversationId, emitter));
        log.debug("SSE: novo emitter de mensagens para conversa {}", conversationId);
        return emitter;
    }

    public void broadcastMessage(String conversationId, MessageDTO message) {
        List<SseEmitter> emitters = messageEmitters.get(conversationId);
        if (emitters == null || emitters.isEmpty()) return;
        List<SseEmitter> dead = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("new-message").data(message));
            } catch (IOException e) {
                log.warn("SSE: falha ao enviar mensagem — removendo emitter");
                dead.add(emitter);
            }
        }
        emitters.removeAll(dead);
    }

    public SseEmitter createConversationEmitter(String operatorId) {
        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        List<SseEmitter> emitters = conversationEmitters.computeIfAbsent(operatorId, k -> new CopyOnWriteArrayList<>());
        emitters.add(emitter);
        emitter.onCompletion(() -> removeConversationEmitter(operatorId, emitter));
        emitter.onTimeout(() -> removeConversationEmitter(operatorId, emitter));
        emitter.onError(e -> removeConversationEmitter(operatorId, emitter));
        log.debug("SSE: novo emitter de conversas para operador {}", operatorId);
        return emitter;
    }

    public void broadcastConversationUpdate(String operatorId, Object data) {
        List<SseEmitter> emitters = conversationEmitters.get(operatorId);
        if (emitters == null || emitters.isEmpty()) return;
        List<SseEmitter> dead = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("conversation-update").data(data));
            } catch (IOException e) {
                log.warn("SSE: falha ao enviar atualização de conversa — removendo emitter");
                dead.add(emitter);
            }
        }
        emitters.removeAll(dead);
    }

    private void removeMessageEmitter(String conversationId, SseEmitter emitter) {
        List<SseEmitter> emitters = messageEmitters.get(conversationId);
        if (emitters != null) emitters.remove(emitter);
    }

    private void removeConversationEmitter(String operatorId, SseEmitter emitter) {
        List<SseEmitter> emitters = conversationEmitters.get(operatorId);
        if (emitters != null) emitters.remove(emitter);
    }
}
