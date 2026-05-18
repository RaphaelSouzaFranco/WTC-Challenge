package com.wtc.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * Configuração MongoDB — habilita auditing automático.
 *
 * Com @EnableMongoAuditing, os campos anotados com:
 * - @CreatedDate → preenchidos automaticamente ao inserir
 * - @LastModifiedDate → atualizados automaticamente ao salvar
 *
 * Usado nos models: Client, Operator, Conversation, Message, Campaign
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
    // A configuração da URI do MongoDB é feita via application.properties
    // spring.data.mongodb.uri=mongodb://localhost:27017/wtcdb
}
