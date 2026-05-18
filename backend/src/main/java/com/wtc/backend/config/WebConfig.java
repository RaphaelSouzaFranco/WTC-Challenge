package com.wtc.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuração de CORS e recursos estáticos (uploads).
 *
 * CORS liberado para:
 * - Emulador Android: http://10.0.2.2:8080
 * - Dispositivo físico: qualquer IP na rede local
 * - Desenvolvimento web: http://localhost:*
 *
 * Em produção, restrinja as origens conforme necessário.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura o handler de recursos estáticos para servir
     * os arquivos de upload (mídia de mensagens e campanhas)
     * via URL: GET /uploads/{filename}
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Origens permitidas
        config.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://10.0.2.2:*",    // Emulador Android → host machine
                "http://192.168.*.*:*"  // Dispositivos físicos na rede local
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With"
        ));

        config.setExposedHeaders(List.of(
                "Authorization",
                "Content-Disposition"
        ));

        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
