package com.wtc.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class ABTestRequest {
    @NotBlank(message = "Titulo da variante B é obrigatório")
    private String tituloB;
    @NotBlank(message = "Mensagem da variante B é obrigatória")
    private String mensagemB;

    public String getTituloB() { return tituloB; }
    public void setTituloB(String tituloB) { this.tituloB = tituloB; }
    public String getMensagemB() { return mensagemB; }
    public void setMensagemB(String mensagemB) { this.mensagemB = mensagemB; }
}
