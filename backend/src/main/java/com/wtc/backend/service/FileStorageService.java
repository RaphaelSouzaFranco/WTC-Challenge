package com.wtc.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Service de armazenamento de arquivos de mídia.
 * Salva uploads em subpastas de uploads/:
 * - uploads/media/     → mídias de mensagens
 * - uploads/campaigns/ → imagens de campanhas
 * - uploads/avatars/   → fotos de perfil
 */
@Service
public class FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String save(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Arquivo vazio ou nulo");

        String originalName = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
        String extension = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex >= 0) extension = originalName.substring(dotIndex);
        String filename = UUID.randomUUID().toString() + extension;

        try {
            Path targetDir = Paths.get(uploadDir, subDir);
            Files.createDirectories(targetDir);
            Files.copy(file.getInputStream(), targetDir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            String url = "/uploads/" + subDir + "/" + filename;
            log.info("Arquivo salvo: {} → {}", originalName, url);
            return url;
        } catch (IOException e) {
            log.error("Erro ao salvar arquivo {}: {}", originalName, e.getMessage());
            throw new RuntimeException("Falha ao salvar arquivo: " + e.getMessage(), e);
        }
    }

    public void delete(String fileUrl) {
        if (!StringUtils.hasText(fileUrl)) return;
        try {
            String relativePath = fileUrl.startsWith("/") ? fileUrl.substring(1) : fileUrl;
            Path path = Paths.get(relativePath);
            if (Files.exists(path)) { Files.delete(path); log.info("Arquivo deletado: {}", fileUrl); }
        } catch (IOException e) {
            log.warn("Não foi possível deletar arquivo {}: {}", fileUrl, e.getMessage());
        }
    }
}
