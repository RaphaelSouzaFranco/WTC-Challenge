package com.wtc.backend.controller;

import com.wtc.backend.dto.DeeplinkMessageRequest;
import com.wtc.backend.dto.MessageDTO;
import com.wtc.backend.dto.MessageRequest;
import com.wtc.backend.service.MessageService;
import com.wtc.backend.service.SseService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

/**
 * Controller REST para Mensagens.
 *
 * Endpoints:
 * GET    /api/messages/{conversationId}           → histórico paginado
 * POST   /api/messages/{conversationId}           → envia texto
 * POST   /api/messages/{conversationId}/media     → envia mídia
 * POST   /api/messages/{conversationId}/deeplink  → envia deeplink
 * PUT    /api/messages/{messageId}/read           → marca como lido
 * GET    /api/messages/{conversationId}/stream    → SSE em tempo real
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final SseService sseService;

    public MessageController(MessageService messageService, SseService sseService) {
        this.messageService = messageService;
        this.sseService = sseService;
    }

    /**
     * GET /api/messages/{conversationId}?page=0&size=30
     *
     * Retorna histórico de mensagens paginado, ordenado por data.
     * Response 200: Page<MessageDTO>
     */
    @GetMapping("/{conversationId}")
    public ResponseEntity<Page<MessageDTO>> getMessages(
            @PathVariable String conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {

        return ResponseEntity.ok(messageService.getMessages(conversationId, page, size));
    }

    /**
     * POST /api/messages/{conversationId}
     *
     * Envia mensagem de texto (type = TEXT).
     *
     * Request body:
     * {
     *   "senderId": "operatorId ou clientId",
     *   "senderType": "OPERATOR" | "CLIENT",
     *   "content": "Texto da mensagem",
     *   "type": "TEXT"
     * }
     *
     * Response 201: MessageDTO
     * Também notifica via SSE todos os emitters da conversa.
     */
    @PostMapping("/{conversationId}")
    public ResponseEntity<MessageDTO> sendText(
            @PathVariable String conversationId,
            @Valid @RequestBody MessageRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.sendText(conversationId, request));
    }

    /**
     * POST /api/messages/{conversationId}/media
     *
     * Envia mensagem com mídia (imagem, vídeo, arquivo).
     * Content-Type: multipart/form-data
     *
     * Form fields:
     * - file (required): arquivo de mídia
     * - senderId (required): ID do remetente
     * - senderType (required): "OPERATOR" ou "CLIENT"
     * - caption (optional): legenda da mídia
     *
     * Response 201: MessageDTO com mediaUrl preenchida
     * O campo mediaUrl é acessível via GET /uploads/media/{filename}
     */
    @PostMapping("/{conversationId}/media")
    public ResponseEntity<MessageDTO> sendMedia(
            @PathVariable String conversationId,
            @RequestParam String senderId,
            @RequestParam String senderType,
            @RequestParam(required = false) String caption,
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.sendMedia(conversationId, senderId, senderType, caption, file));
    }

    /**
     * POST /api/messages/{conversationId}/deeplink
     *
     * Envia mensagem com deeplink interativo.
     * Corresponde ao TrackOrderButton do ChatComponentes.kt.
     *
     * Request body:
     * {
     *   "senderId": "...",
     *   "senderType": "OPERATOR",
     *   "content": "Seu pedido está em trânsito. Rastreie aqui:",
     *   "deeplinkUrl": "wtcapp://track-order/12345",
     *   "deeplinkLabel": "Acompanhar pedido"
     * }
     *
     * Response 201: MessageDTO com type=DEEPLINK
     */
    @PostMapping("/{conversationId}/deeplink")
    public ResponseEntity<MessageDTO> sendDeeplink(
            @PathVariable String conversationId,
            @Valid @RequestBody DeeplinkMessageRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.sendDeeplink(conversationId, request));
    }

    /**
     * PUT /api/messages/{messageId}/read
     *
     * Marca a mensagem como lida (readAt = agora).
     * Response 200: MessageDTO com readAt preenchido
     */
    @PutMapping("/{messageId}/read")
    public ResponseEntity<MessageDTO> markRead(@PathVariable String messageId) {
        return ResponseEntity.ok(messageService.markRead(messageId));
    }

    /**
     * GET /api/messages/{conversationId}/stream
     *
     * Server-Sent Events — stream de novas mensagens em tempo real.
     *
     * O app deve conectar a este endpoint após abrir o chat (SupportScreen.kt).
     * Cada nova mensagem é emitida como evento "new-message" com payload MessageDTO.
     *
     * Eventos:
     * event: new-message
     * data: { MessageDTO JSON }
     *
     * Integração Android sugerida: OkHttp EventSource ou Retrofit Streaming.
     * A conexão é mantida por até 30 minutos e reconectada pelo cliente.
     */
    @GetMapping(value = "/{conversationId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamMessages(@PathVariable String conversationId) {
        return sseService.createMessageEmitter(conversationId);
    }
}
