package com.wtc.backend.controller;

import com.wtc.backend.dto.LoginRequest;
import com.wtc.backend.dto.LoginResponse;
import com.wtc.backend.dto.RegisterRequest;
import com.wtc.backend.service.AuthService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
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
    /**
     * POST /api/auth/register
     *
     * Request:
     * {
     *   "nome": "João Silva",
     *   "email": "joao@wtc.com",
     *   "senha": "minhasenha"
     * }
     *
     * Response 201: OperatorDTO do operador criado
     * Response 409: e-mail já cadastrado
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (authService.emailJaCadastrado(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "E-mail já cadastrado."));
        }
        authService.createOperator(
                request.getNome(),
                request.getEmail(),
                request.getSenha(),
                request.getCargo()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Operador cadastrado com sucesso."));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of("message", "Logout realizado com sucesso"));
    }
}
