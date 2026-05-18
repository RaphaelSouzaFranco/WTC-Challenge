package com.wtc.backend.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class ClientRequest {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @NotBlank(message = "Número é obrigatório")
    private String numero;
    private String ramo;
    private String status;
    private List<String> tags;
    private int score;
    private String operatorId;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getRamo() { return ramo; }
    public void setRamo(String ramo) { this.ramo = ramo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
}
