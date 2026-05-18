package com.wtc.backend.controller;

import com.wtc.backend.dto.ConversationDTO;
import com.wtc.backend.service.ConversationService;
import com.wtc.backend.service.SseService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

/**
 * Controller REST para Conversas.
 *
 * Endpoints:
 * GET    /api/conversations                     → lista por operador com filtro
 * GET    /api/conversations/{id}                → busca por ID
 * POST   /api/conversations                     → cria conversa
 * DELETE /api/conversations/{id}                → remove conversa
 * GET    /api/conversations/stream              → SSE stream de atualizações
 */
@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;
    private final SseService sseService;

    public ConversationController(ConversationService conversationService, SseService sseService) {
        this.conversationService = conversationService;
        this.sseService = sseService;
    }

    /**
     * GET /api/conversations?operatorId={id}&filter={Todos|Clientes|Grupos}
     *
     * Filtros da MessagesScreen.kt (SelectableButton):
     * - "Todos"    → todas as conversas
     * - "Clientes" → isGroup = false
     * - "Grupos"   → isGroup = true
     *
     * Response 200: List<ConversationDTO>
     * Cada ConversationDTO inclui ClientDTO embutido para exibição
     * no ConversationItem sem chamada adicional.
     */
    @GetMapping
    public ResponseEntity<List<ConversationDTO>> getByOperator(
            @RequestParam String operatorId,
            @RequestParam(required = false, defaultValue = "Todos") String filter) {

        return ResponseEntity.ok(conversationService.getByOperator(operatorId, filter));
    }

    /**
     * GET /api/conversations/{id}
     *
     * Response 200: ConversationDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConversationDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(conversationService.getById(id));
    }

    /**
     * POST /api/conversations
     *
     * Cria nova conversa entre operador e cliente.
     * Se já existir, retorna a existente (idempotente).
     *
     * Request body:
     * {
     *   "clientId": "...",
     *   "operatorId": "..."
     * }
     *
     * Response 201: ConversationDTO
     */
    @PostMapping
    public ResponseEntity<ConversationDTO> create(@RequestBody Map<String, String> body) {
        String clientId = body.get("clientId");
        String operatorId = body.get("operatorId");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(conversationService.create(clientId, operatorId));
    }

    /**
     * DELETE /api/conversations/{id}
     *
     * Response 200: { "message": "Conversa removida com sucesso" }
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        conversationService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Conversa removida com sucesso"));
    }

    /**
     * GET /api/conversations/stream?operatorId={id}
     *
     * Server-Sent Events — stream de atualizações de conversas em tempo real.
     * O app deve escutar este endpoint para atualizar:
     * - Badges de mensagens não lidas na MessagesScreen
     * - Preview da última mensagem no ConversationItem
     *
     * Eventos emitidos:
     * - "conversation-update": ConversationDTO atualizado
     *
     * O Android pode consumir com OkHttp EventSource ou
     * uma biblioteca de SSE (ex: okhttp-sse).
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamConversations(@RequestParam String operatorId) {
        return sseService.createConversationEmitter(operatorId);
    }
}
