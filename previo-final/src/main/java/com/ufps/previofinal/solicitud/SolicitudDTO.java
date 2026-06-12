package com.ufps.previofinal.solicitud;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SolicitudDTO {

    @NotNull(message = "El tipo es obligatorio. Valores: SOPORTE, ACCESO, INFORMACION")
    private TipoSolicitud tipo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    public TipoSolicitud getTipo() { return tipo; }
    public void setTipo(TipoSolicitud tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
