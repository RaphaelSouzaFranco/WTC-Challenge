package com.wtc.backend.repository;

import com.wtc.backend.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório Spring Data MongoDB para a coleção "messages".
 *
 * Suporta os tipos de mensagem do SupportScreen.kt:
 * - TEXT: UserMessage / SupportMessage
 * - MEDIA: imagens e arquivos (ChatBottomBar)
 * - DEEPLINK: botões interativos (TrackOrderButton)
 */
@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    /**
     * Busca mensagens de uma conversa paginadas, ordenadas por data.
     * Usado para carregar o histórico do chat (paginação para não sobrecarregar).
     */
    Page<Message> findByConversationIdOrderByCreatedAtAsc(String conversationId, Pageable pageable);

    /**
     * Busca todas as mensagens de uma conversa (sem paginação).
     * Usado internamente pelo SseService para streaming.
     */
    List<Message> findByConversationIdOrderByCreatedAtAsc(String conversationId);

    /**
     * Busca mensagens não lidas de uma conversa para um remetente específico.
     * readAt = null significa mensagem não lida.
     */
    List<Message> findByConversationIdAndSenderTypeAndReadAtIsNull(
            String conversationId, String senderType);

    /** Conta mensagens não lidas numa conversa */
    long countByConversationIdAndReadAtIsNull(String conversationId);

    /** Busca as últimas N mensagens de uma conversa (para preview) */
    List<Message> findTop1ByConversationIdOrderByCreatedAtDesc(String conversationId);
}
