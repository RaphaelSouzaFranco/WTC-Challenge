package com.wtc.backend.controller;

import com.wtc.backend.dto.OperatorDTO;
import com.wtc.backend.service.OperatorService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Controller REST para Operadores.
 * Mapeado à ProfileScreen.kt do Android.
 *
 * Endpoints:
 * GET  /api/operators/{id}          → busca perfil
 * PUT  /api/operators/{id}          → atualiza perfil
 * PUT  /api/operators/{id}/avatar   → upload de foto de perfil
 */
@RestController
@RequestMapping("/api/operators")
public class OperatorController {

    private final OperatorService operatorService;

    public OperatorController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    /**
     * GET /api/operators/{id}
     *
     * Response 200: OperatorDTO
     * {
     *   "id": "...",
     *   "nome": "Isabella Rossi",
     *   "email": "isabella@wtc.com",
     *   "cargo": "Customer Support",
     *   "avatarUrl": "/uploads/avatars/uuid.jpg",
     *   "darkMode": true,
     *   "notas": "Anotações do operador..."
     * }
     */
    @GetMapping("/{id}")
    public ResponseEntity<OperatorDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(operatorService.getById(id));
    }

    /**
     * PUT /api/operators/{id}
     *
     * Atualiza campos do perfil.
     * Mapeado ao BasicTextField "Notas" e Switch "Modo Escuro" da ProfileContent.kt.
     *
     * Request body (todos opcionais):
     * {
     *   "nome": "...",
     *   "cargo": "...",
     *   "notas": "...",
     *   "darkMode": true
     * }
     *
     * Response 200: OperatorDTO atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<OperatorDTO> update(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {

        String nome = (String) body.get("nome");
        String cargo = (String) body.get("cargo");
        String notas = (String) body.get("notas");
        Boolean darkMode = body.get("darkMode") != null
                ? Boolean.valueOf(body.get("darkMode").toString())
                : null;

        return ResponseEntity.ok(operatorService.update(id, nome, cargo, notas, darkMode));
    }

    /**
     * PUT /api/operators/{id}/avatar
     *
     * Upload de foto de perfil.
     * Content-Type: multipart/form-data
     *
     * Form field:
     * - file (required): imagem (JPG, PNG)
     *
     * Response 200: { "avatarUrl": "/uploads/avatars/{filename}" }
     */
    @PutMapping("/{id}/avatar")
    public ResponseEntity<Map<String, String>> updateAvatar(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {

        String avatarUrl = operatorService.updateAvatar(id, file);
        return ResponseEntity.ok(Map.of("avatarUrl", avatarUrl));
    }
}
