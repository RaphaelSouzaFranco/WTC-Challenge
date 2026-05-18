package com.wtc.backend.service;

import com.wtc.backend.dto.MessageDTO;
import com.wtc.backend.dto.DeeplinkMessageRequest;
import com.wtc.backend.dto.MessageRequest;
import com.wtc.backend.model.Message;
import com.wtc.backend.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;
    private final SseService sseService;
    private final FileStorageService fileStorageService;

    public MessageService(MessageRepository messageRepository, ConversationService conversationService,
                          SseService sseService, FileStorageService fileStorageService) {
        this.messageRepository = messageRepository; this.conversationService = conversationService;
        this.sseService = sseService; this.fileStorageService = fileStorageService;
    }

    public Page<MessageDTO> getMessages(String conversationId, int page, int size) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(
                conversationId, PageRequest.of(page, size)).map(this::toDTO);
    }

    public MessageDTO sendText(String conversationId, MessageRequest request) {
        Message message = Message.builder().conversationId(conversationId)
                .senderId(request.getSenderId()).senderType(request.getSenderType())
                .type("TEXT").content(request.getContent()).build();
        MessageDTO dto = toDTO(messageRepository.save(message));
        conversationService.updateLastMessage(conversationId, request.getContent());
        sseService.broadcastMessage(conversationId, dto);
        return dto;
    }

    public MessageDTO sendMedia(String conversationId, String senderId, String senderType,
                                String caption, MultipartFile file) {
        String mediaUrl = fileStorageService.save(file, "media");
        Message message = Message.builder().conversationId(conversationId)
                .senderId(senderId).senderType(senderType).type("MEDIA")
                .content(caption).mediaUrl(mediaUrl).mediaType(file.getContentType()).build();
        MessageDTO dto = toDTO(messageRepository.save(message));
        conversationService.updateLastMessage(conversationId, "📎 Mídia");
        sseService.broadcastMessage(conversationId, dto);
        return dto;
    }

    public MessageDTO sendDeeplink(String conversationId, DeeplinkMessageRequest request) {
        Message message = Message.builder().conversationId(conversationId)
                .senderId(request.getSenderId()).senderType(request.getSenderType())
                .type("DEEPLINK").content(request.getContent())
                .deeplinkUrl(request.getDeeplinkUrl()).deeplinkLabel(request.getDeeplinkLabel()).build();
        MessageDTO dto = toDTO(messageRepository.save(message));
        conversationService.updateLastMessage(conversationId, "🔗 " + request.getDeeplinkLabel());
        sseService.broadcastMessage(conversationId, dto);
        return dto;
    }

    public MessageDTO markRead(String messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mensagem não encontrada: " + messageId));
        if (message.getReadAt() == null) { message.setReadAt(Instant.now()); message = messageRepository.save(message); }
        return toDTO(message);
    }

    public MessageDTO toDTO(Message m) {
        return MessageDTO.builder().id(m.getId()).conversationId(m.getConversationId())
                .senderId(m.getSenderId()).senderType(m.getSenderType()).type(m.getType())
                .content(m.getContent()).mediaUrl(m.getMediaUrl()).mediaType(m.getMediaType())
                .deeplinkUrl(m.getDeeplinkUrl()).deeplinkLabel(m.getDeeplinkLabel())
                .readAt(m.getReadAt()).createdAt(m.getCreatedAt()).build();
    }
}
