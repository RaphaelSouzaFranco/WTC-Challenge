package com.wtc.backend.service;

import com.wtc.backend.dto.ClientDTO;
import com.wtc.backend.dto.ClientRequest;
import com.wtc.backend.model.Client;
import com.wtc.backend.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientDTO> getClients(String search, String status, Integer minScore, Integer maxScore, String tag) {
        List<Client> clients;
        if (StringUtils.hasText(tag)) {
            clients = clientRepository.findByTagsContaining(tag);
        } else if (StringUtils.hasText(search)) {
            clients = clientRepository.findByNomeOrRamoContaining(search);
        } else if (StringUtils.hasText(status)) {
            clients = clientRepository.findByStatus(status);
        } else {
            clients = clientRepository.findAll();
        }
        int min = (minScore != null) ? minScore : 0;
        int max = (maxScore != null) ? maxScore : 100;
        return clients.stream()
                .filter(c -> c.getScore() >= min && c.getScore() <= max)
                .map(this::toDTO).collect(Collectors.toList());
    }

    public ClientDTO getById(String id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + id));
        return toDTO(client);
    }

    public ClientDTO create(ClientRequest request) {
        Client client = Client.builder()
                .nome(request.getNome()).numero(request.getNumero()).ramo(request.getRamo())
                .status(request.getStatus() != null ? request.getStatus() : "Lead")
                .tags(request.getTags()).score(request.getScore()).operatorId(request.getOperatorId())
                .build();
        return toDTO(clientRepository.save(client));
    }

    public ClientDTO update(String id, ClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + id));
        client.setNome(request.getNome()); client.setNumero(request.getNumero());
        client.setRamo(request.getRamo()); client.setStatus(request.getStatus());
        client.setTags(request.getTags()); client.setScore(request.getScore());
        return toDTO(clientRepository.save(client));
    }

    public void delete(String id) {
        if (!clientRepository.existsById(id)) throw new RuntimeException("Cliente não encontrado: " + id);
        clientRepository.deleteById(id);
    }

    public ClientDTO toDTO(Client client) {
        return ClientDTO.builder()
                .id(client.getId()).nome(client.getNome()).numero(client.getNumero())
                .ramo(client.getRamo()).status(client.getStatus()).tags(client.getTags())
                .score(client.getScore()).operatorId(client.getOperatorId())
                .createdAt(client.getCreatedAt()).updatedAt(client.getUpdatedAt()).build();
    }
}
