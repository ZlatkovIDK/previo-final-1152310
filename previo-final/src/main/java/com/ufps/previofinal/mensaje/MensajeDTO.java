package com.ufps.previofinal.mensaje;

import jakarta.validation.constraints.NotBlank;

public class MensajeDTO {

    @NotBlank(message = "El destinatario es obligatorio")
    private String usernameReceptor;

    @NotBlank(message = "El asunto es obligatorio")
    private String asunto;

    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;

    public String getUsernameReceptor() { return usernameReceptor; }
    public void setUsernameReceptor(String usernameReceptor) { this.usernameReceptor = usernameReceptor; }
    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
}
