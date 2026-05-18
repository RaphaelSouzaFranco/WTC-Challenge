package com.wtc.backend.repository;

import com.wtc.backend.model.Campaign;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório Spring Data MongoDB para a coleção "campaigns".
 * Suporta os fluxos da CampaignScreen.kt.
 */
@Repository
public interface CampaignRepository extends MongoRepository<Campaign, String> {

    /** Busca todas as campanhas de um operador */
    List<Campaign> findByOperatorIdOrderByCreatedAtDesc(String operatorId);

    /** Busca campanhas por status (DRAFT, SENT, SCHEDULED) */
    List<Campaign> findByOperatorIdAndStatus(String operatorId, String status);

    /** Busca campanhas por público-alvo */
    List<Campaign> findByTargetAudience(String targetAudience);
}
