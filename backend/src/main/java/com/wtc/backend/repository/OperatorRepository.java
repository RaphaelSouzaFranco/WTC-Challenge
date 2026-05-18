package com.wtc.backend.repository;

import com.wtc.backend.model.Operator;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório Spring Data MongoDB para a coleção "operators".
 * Usado pelo AuthService para login e pelo OperatorService para perfil.
 */
@Repository
public interface OperatorRepository extends MongoRepository<Operator, String> {

    /** Busca operador por e-mail — usado no login (LoginScreen.kt) */
    Optional<Operator> findByEmail(String email);

    /** Verifica se o e-mail já está cadastrado */
    boolean existsByEmail(String email);
}
