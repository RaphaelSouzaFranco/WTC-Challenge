package com.wtc.backend.service;

import com.wtc.backend.dto.ConversationDTO;
import com.wtc.backend.dto.MessageDTO;
import com.wtc.backend.model.Conversation;
import com.wtc.backend.model.Message;
import com.wtc.backend.repository.ConversationRepository;
import com.wtc.backend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class InboxService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;
    private final ConversationService conversationService;

    public InboxService(ConversationRepository conversationRepository,
                        MessageRepository messageRepository,
                        MessageService messageService,
                        ConversationService conversationService) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.messageService = messageService;
        this.conversationService = conversationService;
    }

    public List<ConversationDTO> getConversationsForClient(String clientId) {
        List<Conversation> conversations = conversationRepository.findByClientIdOrderByLastMessageAtDesc(clientId);
        return conversations.stream().map(conversationService::toDTO).toList();
    }

    public List<MessageDTO> getMessagesForClient(String clientId, int limit) {
        List<Conversation> conversations = conversationRepository.findByClientIdOrderByLastMessageAtDesc(clientId);
        List<Message> allMessages = new ArrayList<>();
        for (Conversation conv : conversations) {
            allMessages.addAll(messageRepository.findByConversationIdOrderByCreatedAtAsc(conv.getId()));
        }
        allMessages.sort(Comparator.comparing(Message::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())));
        return allMessages.stream()
                .limit(limit)
                .map(messageService::toDTO)
                .toList();
    }
}
