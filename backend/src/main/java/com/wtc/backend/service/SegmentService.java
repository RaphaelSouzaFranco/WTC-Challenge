package com.wtc.backend.service;

import com.wtc.backend.dto.SegmentDTO;
import com.wtc.backend.dto.SegmentRequest;
import com.wtc.backend.model.Client;
import com.wtc.backend.model.Segment;
import com.wtc.backend.repository.ClientRepository;
import com.wtc.backend.repository.SegmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SegmentService {

    private final SegmentRepository segmentRepository;
    private final ClientRepository clientRepository;

    public SegmentService(SegmentRepository segmentRepository, ClientRepository clientRepository) {
        this.segmentRepository = segmentRepository;
        this.clientRepository = clientRepository;
    }

    public List<SegmentDTO> getByOperator(String operatorId) {
        return segmentRepository.findByOperatorIdOrderByCreatedAtDesc(operatorId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public SegmentDTO getById(String id) {
        Segment segment = segmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Segmento não encontrado: " + id));
        return toDTO(segment);
    }

    public SegmentDTO create(SegmentRequest request) {
        List<String> resolvedClientIds = resolveClients(request.getCriterios(), request.getClientIds());
        Segment segment = Segment.builder()
                .nome(request.getNome()).descricao(request.getDescricao())
                .operatorId(request.getOperatorId()).criterios(request.getCriterios())
                .clientIds(resolvedClientIds).build();
        return toDTO(segmentRepository.save(segment));
    }

    public SegmentDTO update(String id, SegmentRequest request) {
        Segment segment = segmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Segmento não encontrado: " + id));
        segment.setNome(request.getNome());
        segment.setDescricao(request.getDescricao());
        segment.setCriterios(request.getCriterios());
        List<String> resolvedClientIds = resolveClients(request.getCriterios(), request.getClientIds());
        segment.setClientIds(resolvedClientIds);
        return toDTO(segmentRepository.save(segment));
    }

    public void delete(String id) {
        if (!segmentRepository.existsById(id)) throw new RuntimeException("Segmento não encontrado: " + id);
        segmentRepository.deleteById(id);
    }

    public List<String> getClientIdsForSegment(String segmentId) {
        Segment segment = segmentRepository.findById(segmentId)
                .orElseThrow(() -> new RuntimeException("Segmento não encontrado: " + segmentId));
        return segment.getClientIds() != null ? segment.getClientIds() : Collections.emptyList();
    }

    private List<String> resolveClients(Map<String, Object> criterios, List<String> explicitIds) {
        if (explicitIds != null && !explicitIds.isEmpty()) {
            return explicitIds;
        }
        if (criterios == null || criterios.isEmpty()) {
            return new ArrayList<>();
        }
        List<Client> all = clientRepository.findAll();
        return all.stream().filter(c -> matchesCriteria(c, criterios))
                .map(Client::getId).collect(Collectors.toList());
    }

    private boolean matchesCriteria(Client client, Map<String, Object> criterios) {
        Object status = criterios.get("status");
        if (status != null && StringUtils.hasText(status.toString())
                && !status.toString().equals(client.getStatus())) {
            return false;
        }
        Object tag = criterios.get("tag");
        if (tag != null && StringUtils.hasText(tag.toString())
                && (client.getTags() == null || !client.getTags().contains(tag.toString()))) {
            return false;
        }
        Object minScore = criterios.get("minScore");
        if (minScore != null && client.getScore() < ((Number) minScore).intValue()) {
            return false;
        }
        Object maxScore = criterios.get("maxScore");
        if (maxScore != null && client.getScore() > ((Number) maxScore).intValue()) {
            return false;
        }
        Object ramo = criterios.get("ramo");
        if (ramo != null && StringUtils.hasText(ramo.toString())
                && !ramo.toString().equalsIgnoreCase(client.getRamo())) {
            return false;
        }
        return true;
    }

    public SegmentDTO toDTO(Segment s) {
        return SegmentDTO.builder()
                .id(s.getId()).nome(s.getNome()).descricao(s.getDescricao())
                .operatorId(s.getOperatorId()).criterios(s.getCriterios())
                .clientIds(s.getClientIds())
                .clientCount(s.getClientIds() != null ? s.getClientIds().size() : 0)
                .createdAt(s.getCreatedAt()).updatedAt(s.getUpdatedAt()).build();
    }
}
