package com.wtc.backend.controller;

import com.wtc.backend.dto.ABTestRequest;
import com.wtc.backend.dto.CampaignDTO;
import com.wtc.backend.dto.CampaignRequest;
import com.wtc.backend.dto.ScheduleRequest;
import com.wtc.backend.service.CampaignService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Controller REST para Campanhas Expressas.
 * Mapeado à CampaignScreen.kt do Android.
 *
 * Endpoints:
 * GET    /api/campaigns                   → lista campanhas do operador
 * GET    /api/campaigns/{id}              → busca por ID
 * POST   /api/campaigns                   → cria campanha (status=DRAFT)
 * POST   /api/campaigns/{id}/media        → upload de imagem
 * POST   /api/campaigns/{id}/send         → envia campanha (DRAFT→SENT)
 * DELETE /api/campaigns/{id}              → remove campanha
 */
@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    /**
     * GET /api/campaigns?operatorId={id}&status={DRAFT|SENT}
     *
     * Response 200: List<CampaignDTO>
     */
    @GetMapping
    public ResponseEntity<List<CampaignDTO>> getByOperator(
            @RequestParam String operatorId,
            @RequestParam(required = false) String status) {

        return ResponseEntity.ok(campaignService.getByOperator(operatorId, status));
    }

    /**
     * GET /api/campaigns/{id}
     *
     * Response 200: CampaignDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<CampaignDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(campaignService.getById(id));
    }

    /**
     * POST /api/campaigns
     *
     * Cria campanha no status DRAFT.
     * Mapeado ao botão "Send Now" da CampaignScreen.kt.
     *
     * Request body:
     * {
     *   "titulo": "Promoção de Verão",
     *   "mensagem": "Aproveite nossos descontos exclusivos!",
     *   "targetAudience": "Simple",
     *   "operatorId": "..."
     * }
     *
     * Response 201: CampaignDTO com status=DRAFT
     */
    @PostMapping
    public ResponseEntity<CampaignDTO> create(@Valid @RequestBody CampaignRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(campaignService.create(request));
    }

    /**
     * POST /api/campaigns/{id}/media
     *
     * Upload da imagem da campanha (ImageUploadBox da CampaignScreen.kt).
     * Content-Type: multipart/form-data
     *
     * Form field:
     * - file (required): imagem (JPG, PNG)
     *
     * Response 200: { "mediaUrl": "/uploads/campaigns/{filename}" }
     */
    @PostMapping("/{id}/media")
    public ResponseEntity<Map<String, String>> uploadMedia(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {

        CampaignDTO updated = campaignService.uploadMedia(id, file);
        return ResponseEntity.ok(Map.of("mediaUrl", updated.getMediaUrl()));
    }

    /**
     * POST /api/campaigns/{id}/send
     *
     * Envia a campanha: muda status de DRAFT para SENT.
     * Registra o timestamp de envio (sentAt).
     *
     * Response 200: CampaignDTO com status=SENT e sentAt preenchido
     */
    @PostMapping("/{id}/send")
    public ResponseEntity<CampaignDTO> send(@PathVariable String id) {
        return ResponseEntity.ok(campaignService.send(id));
    }

    /**
     * DELETE /api/campaigns/{id}
     *
     * Remove a campanha e deleta o arquivo de mídia associado.
     * Response 200: { "message": "Campanha removida com sucesso" }
     */
    @PostMapping("/{id}/schedule")
    public ResponseEntity<CampaignDTO> schedule(
            @PathVariable String id,
            @Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(campaignService.schedule(id, request));
    }

    @PostMapping("/{id}/abtest")
    public ResponseEntity<List<CampaignDTO>> abtest(
            @PathVariable String id,
            @Valid @RequestBody ABTestRequest request) {
        return ResponseEntity.ok(campaignService.createABTest(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String id) {
        campaignService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Campanha removida com sucesso"));
    }
}
