package com.wtc.backend.controller;

import com.wtc.backend.dto.ClientDTO;
import com.wtc.backend.dto.ClientRequest;
import com.wtc.backend.service.ClientService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller REST para Clientes.
 *
 * Endpoints:
 * GET    /api/clients                  → lista com filtros opcionais
 * GET    /api/clients/{id}             → busca por ID
 * POST   /api/clients                  → cria cliente
 * PUT    /api/clients/{id}             → atualiza cliente
 * DELETE /api/clients/{id}             → remove cliente
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * GET /api/clients
     *
     * Query params opcionais:
     * - search:   texto para busca por nome ou ramo
     * - status:   "Ativo", "Inativo", "Lead", "Prospect"
     * - minScore: score mínimo (0-100)
     * - maxScore: score máximo (0-100)
     * - tag:      filtro por tag específica
     *
     * Response 200: List<ClientDTO>
     */
    @GetMapping
    public ResponseEntity<List<ClientDTO>> getClients(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer minScore,
            @RequestParam(required = false) Integer maxScore,
            @RequestParam(required = false) String tag) {

        return ResponseEntity.ok(clientService.getClients(search, status, minScore, maxScore, tag));
    }

    /**
     * GET /api/clients/{id}
     *
     * Response 200: ClientDTO
     * Response 404: { "error": "Cliente não encontrado: {id}" }
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(clientService.getById(id));
    }

    /**
     * POST /api/clients
     *
     * Request body:
     * {
     *   "nome": "João Silva",
     *   "numero": "+5511999999999",
     *   "ramo": "Tecnologia",
     *   "status": "Lead",
     *   "tags": ["VIP", "Novo"],
     *   "score": 75,
     *   "operatorId": "..."
     * }
     *
     * Response 201: ClientDTO
     */
    @PostMapping
    public ResponseEntity<ClientDTO> create(@Valid @RequestBody ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.create(request));
    }

    /**
     * PUT /api/clients/{id}
     *
     * Request body: mesmo formato do POST
     * Response 200: ClientDTO atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> update(
            @PathVariable String id,
            @Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(clientService.update(id, request));
    }

    /**
     * DELETE /api/clients/{id}
     *
     * Response 200: { "message": "Cliente removido com sucesso" }
     * Response 404: se não encontrado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        clientService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Cliente removido com sucesso"));
    }
}
