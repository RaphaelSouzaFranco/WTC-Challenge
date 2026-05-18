package com.wtc.backend.service;

import com.wtc.backend.dto.OperatorDTO;
import com.wtc.backend.model.Operator;
import com.wtc.backend.repository.OperatorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OperatorService {

    private final OperatorRepository operatorRepository;
    private final FileStorageService fileStorageService;

    public OperatorService(OperatorRepository operatorRepository, FileStorageService fileStorageService) {
        this.operatorRepository = operatorRepository; this.fileStorageService = fileStorageService;
    }

    public OperatorDTO getById(String id) {
        Operator op = operatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operador não encontrado: " + id));
        return toDTO(op);
    }

    public OperatorDTO update(String id, String nome, String cargo, String notas, Boolean darkMode) {
        Operator op = operatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operador não encontrado: " + id));
        if (nome != null) op.setNome(nome);
        if (cargo != null) op.setCargo(cargo);
        if (notas != null) op.setNotas(notas);
        if (darkMode != null) op.setDarkMode(darkMode);
        return toDTO(operatorRepository.save(op));
    }

    public String updateAvatar(String id, MultipartFile file) {
        Operator op = operatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operador não encontrado: " + id));
        if (op.getAvatarUrl() != null) fileStorageService.delete(op.getAvatarUrl());
        String avatarUrl = fileStorageService.save(file, "avatars");
        op.setAvatarUrl(avatarUrl); operatorRepository.save(op);
        return avatarUrl;
    }

    public OperatorDTO toDTO(Operator op) {
        return OperatorDTO.builder().id(op.getId()).nome(op.getNome()).email(op.getEmail())
                .cargo(op.getCargo()).avatarUrl(op.getAvatarUrl())
                .darkMode(op.isDarkMode()).notas(op.getNotas()).build();
    }
}
