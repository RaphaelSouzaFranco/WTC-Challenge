package com.wtc.backend.repository;

import com.wtc.backend.model.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data MongoDB para a coleção "conversations".
 *
 * Suporta os filtros da MessagesScreen.kt:
 * - "Todos": todas as conversas do operador
 * - "Clientes": isGroup = false
 * - "Grupos": isGroup = true
 */
@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {

    /**
     * Busca todas as conversas de um operador, ordenadas pela última mensagem.
     * Exibidas na MessagesScreen.kt (lista de ConversationItem).
     */
    List<Conversation> findByOperatorIdOrderByLastMessageAtDesc(String operatorId);

    /**
     * Busca conversas individuais (não grupos) de um operador.
     * Filtro "Clientes" na MessagesScreen.
     */
    List<Conversation> findByOperatorIdAndIsGroupFalseOrderByLastMessageAtDesc(String operatorId);

    /**
     * Busca conversas de grupo de um operador.
     * Filtro "Grupos" na MessagesScreen.
     */
    List<Conversation> findByOperatorIdAndIsGroupTrueOrderByLastMessageAtDesc(String operatorId);

    /** Busca conversas de um cliente específico */
    List<Conversation> findByClientIdOrderByLastMessageAtDesc(String clientId);

    /** Busca a conversa de um cliente específico com um operador */
    Optional<Conversation> findByClientIdAndOperatorId(String clientId, String operatorId);

    /** Conta conversas com mensagens não lidas */
    long countByOperatorIdAndUnreadCountGreaterThan(String operatorId, int count);
}
