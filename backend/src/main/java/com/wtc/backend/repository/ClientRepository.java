package com.wtc.backend.repository;

import com.wtc.backend.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data MongoDB para a coleção "clients".
 *
 * Suporta os filtros da ClientListScreen.kt:
 * - Busca por nome ou ramo (campo textoBusca)
 * - Filtro por status
 * - Filtro por score mínimo/máximo (ScoreFilterSlider.kt)
 * - Filtro por tags
 */
@Repository
public interface ClientRepository extends MongoRepository<Client, String> {

    /** Busca todos os clientes de um operador */
    List<Client> findByOperatorId(String operatorId);

    /** Busca por nome contendo texto (case-insensitive) */
    List<Client> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca combinada: nome OU ramo contendo o texto.
     * Mimetiza a lógica de filtro da ClientListScreen.kt:
     * cliente.nome.contains(textoBusca) || cliente.ramo.contains(textoBusca)
     */
    @Query("{ $or: [ { 'nome': { $regex: ?0, $options: 'i' } }, { 'ramo': { $regex: ?0, $options: 'i' } } ] }")
    List<Client> findByNomeOrRamoContaining(String searchTerm);

    /** Busca por status exato */
    List<Client> findByStatus(String status);

    /** Busca por score entre dois valores (ScoreFilterSlider) */
    List<Client> findByScoreBetween(int minScore, int maxScore);

    /** Busca clientes que contêm uma tag específica */
    List<Client> findByTagsContaining(String tag);

    /**
     * Busca combinada com todos os filtros opcionais.
     * Utilizada pelo ClientService para montar queries dinâmicas.
     */
    @Query("{ 'operatorId': ?0, 'status': { $regex: ?1, $options: 'i' }, 'score': { $gte: ?2, $lte: ?3 } }")
    List<Client> findByFilters(String operatorId, String status, int minScore, int maxScore);

    /** Verifica se já existe cliente com o mesmo número */
    Optional<Client> findByNumero(String numero);

    /** Contagem de clientes por operador */
    long countByOperatorId(String operatorId);
}
