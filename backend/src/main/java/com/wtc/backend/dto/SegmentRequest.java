package com.wtc.backend.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

public class SegmentRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    private String descricao;
    @NotBlank(message = "operatorId é obrigatório")
    private String operatorId;
    private Map<String, Object> criterios;
    private List<String> clientIds;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    public Map<String, Object> getCriterios() { return criterios; }
    public void setCriterios(Map<String, Object> criterios) { this.criterios = criterios; }
    public List<String> getClientIds() { return clientIds; }
    public void setClientIds(List<String> clientIds) { this.clientIds = clientIds; }
}
