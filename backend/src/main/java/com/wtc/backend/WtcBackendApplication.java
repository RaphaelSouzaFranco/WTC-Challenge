package com.wtc.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * WTC Challenge Backend
 * REST API para integração com o app Android Kotlin/Jetpack Compose.
 *
 * Funcionalidades:
 * - CRUD de Clientes, Conversas, Mensagens, Campanhas e Operadores
 * - Autenticação JWT
 * - Envio de mensagens com suporte a texto, mídia e deeplinks
 * - Dados em tempo real via Server-Sent Events (SSE)
 * - Integração com MongoDB
 */
@SpringBootApplication
public class WtcBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WtcBackendApplication.class, args);
    }
}
