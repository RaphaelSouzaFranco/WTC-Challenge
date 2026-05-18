package com.wtc.backend.service;

import com.wtc.backend.dto.CampaignDTO;
import com.wtc.backend.dto.CampaignRequest;
import com.wtc.backend.model.Campaign;
import com.wtc.backend.repository.CampaignRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final FileStorageService fileStorageService;

    public CampaignService(CampaignRepository campaignRepository, FileStorageService fileStorageService) {
        this.campaignRepository = campaignRepository; this.fileStorageService = fileStorageService;
    }

    public List<CampaignDTO> getByOperator(String operatorId, String status) {
        List<Campaign> campaigns = (status != null && !status.isBlank())
                ? campaignRepository.findByOperatorIdAndStatus(operatorId, status)
                : campaignRepository.findByOperatorIdOrderByCreatedAtDesc(operatorId);
        return campaigns.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CampaignDTO getById(String id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada: " + id));
        return toDTO(campaign);
    }

    public CampaignDTO create(CampaignRequest request) {
        Campaign campaign = Campaign.builder().titulo(request.getTitulo())
                .mensagem(request.getMensagem()).targetAudience(request.getTargetAudience())
                .status("DRAFT").operatorId(request.getOperatorId()).build();
        return toDTO(campaignRepository.save(campaign));
    }

    public CampaignDTO uploadMedia(String id, MultipartFile file) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada: " + id));
        if (campaign.getMediaUrl() != null) fileStorageService.delete(campaign.getMediaUrl());
        campaign.setMediaUrl(fileStorageService.save(file, "campaigns"));
        return toDTO(campaignRepository.save(campaign));
    }

    public CampaignDTO send(String id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada: " + id));
        campaign.setStatus("SENT"); campaign.setSentAt(Instant.now());
        return toDTO(campaignRepository.save(campaign));
    }

    public void delete(String id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada: " + id));
        if (campaign.getMediaUrl() != null) fileStorageService.delete(campaign.getMediaUrl());
        campaignRepository.deleteById(id);
    }

    public CampaignDTO toDTO(Campaign c) {
        return CampaignDTO.builder().id(c.getId()).titulo(c.getTitulo()).mensagem(c.getMensagem())
                .targetAudience(c.getTargetAudience()).mediaUrl(c.getMediaUrl()).status(c.getStatus())
                .operatorId(c.getOperatorId()).sentAt(c.getSentAt()).createdAt(c.getCreatedAt()).build();
    }
}
