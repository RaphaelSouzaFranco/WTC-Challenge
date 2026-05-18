package com.wtc.backend.service;

import com.wtc.backend.dto.ConversationDTO;
import com.wtc.backend.model.Conversation;
import com.wtc.backend.repository.ClientRepository;
import com.wtc.backend.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ClientRepository clientRepository;
    private final ClientService clientService;
    private final SseService sseService;

    public ConversationService(ConversationRepository conversationRepository,
                               ClientRepository clientRepository,
                               ClientService clientService,
                               SseService sseService) {
        this.conversationRepository = conversationRepository;
        this.clientRepository = clientRepository;
        this.clientService = clientService;
        this.sseService = sseService;
    }

    public List<ConversationDTO> getByOperator(String operatorId, String filter) {
        List<Conversation> conversations;
        switch (filter != null ? filter : "Todos") {
            case "Clientes" -> conversations = conversationRepository.findByOperatorIdAndIsGroupFalseOrderByLastMessageAtDesc(operatorId);
            case "Grupos" -> conversations = conversationRepository.findByOperatorIdAndIsGroupTrueOrderByLastMessageAtDesc(operatorId);
            default -> conversations = conversationRepository.findByOperatorIdOrderByLastMessageAtDesc(operatorId);
        }
        return conversations.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ConversationDTO getById(String id) {
        Conversation conv = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversa não encontrada: " + id));
        return toDTO(conv);
    }

    public ConversationDTO create(String clientId, String operatorId) {
        return conversationRepository.findByClientIdAndOperatorId(clientId, operatorId)
                .map(this::toDTO)
                .orElseGet(() -> {
                    Conversation conv = Conversation.builder()
                            .clientId(clientId).operatorId(operatorId)
                            .unreadCount(0).isGroup(false).build();
                    ConversationDTO created = toDTO(conversationRepository.save(conv));
                    sseService.broadcastConversationUpdate(operatorId, created);
                    return created;
                });
    }

    public void delete(String id) {
        if (!conversationRepository.existsById(id)) throw new RuntimeException("Conversa não encontrada: " + id);
        conversationRepository.deleteById(id);
    }

    public void updateLastMessage(String conversationId, String preview) {
        conversationRepository.findById(conversationId).ifPresent(conv -> {
            conv.setLastMessage(preview);
            conv.setLastMessageAt(Instant.now());
            conv.setUnreadCount(conv.getUnreadCount() + 1);
            conversationRepository.save(conv);
        });
    }

    public void markAllRead(String conversationId) {
        conversationRepository.findById(conversationId).ifPresent(conv -> {
            conv.setUnreadCount(0);
            conversationRepository.save(conv);
        });
    }

    public ConversationDTO toDTO(Conversation conv) {
        ConversationDTO.Builder builder = ConversationDTO.builder()
                .id(conv.getId()).clientId(conv.getClientId()).operatorId(conv.getOperatorId())
                .lastMessage(conv.getLastMessage()).lastMessageAt(conv.getLastMessageAt())
                .unreadCount(conv.getUnreadCount()).isGroup(conv.isGroup()).createdAt(conv.getCreatedAt());
        if (conv.getClientId() != null) {
            clientRepository.findById(conv.getClientId())
                    .ifPresent(client -> builder.client(clientService.toDTO(client)));
        }
        return builder.build();
    }
}
