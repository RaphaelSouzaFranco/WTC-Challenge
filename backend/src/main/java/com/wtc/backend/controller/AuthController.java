package com.wtc.backend.controller;

import com.wtc.backend.dto.LoginRequest;
import com.wtc.backend.dto.LoginResponse;
import com.wtc.backend.service.AuthService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller de autenticação.
 *
 * Endpoints:
 * - POST /api/auth/login  → autentica e retorna JWT + dados do operador
 * - POST /api/auth/logout → invalidação client-side (stateless JWT)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/login
     *
     * Request:
     * {
     *   "email": "isabella@wtc.com",
     *   "senha": "senha123"
     * }
     *
     * Response 200:
     * {
     *   "token": "eyJhbG...",
     *   "tokenType": "Bearer",
     *   "operator": { "id", "nome", "email", "cargo", "avatarUrl", "darkMode", "notas" }
     * }
     *
     * Response 401: credenciais inválidas
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/logout
     *
     * Como JWT é stateless, o logout é feito no lado do cliente
     * (descartando o token). Este endpoint existe para logs/audit.
     *
     * Response 200:
     * { "message": "Logout realizado com sucesso" }
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of("message", "Logout realizado com sucesso"));
    }
}
