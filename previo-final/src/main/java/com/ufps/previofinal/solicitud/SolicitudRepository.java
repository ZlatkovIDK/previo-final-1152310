package com.ufps.previofinal.solicitud;

import com.ufps.previofinal.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findBySolicitanteOrderByFechaCreacionDesc(Usuario solicitante);
    List<Solicitud> findAllByOrderByFechaCreacionDesc();
    long countByEstado(EstadoSolicitud estado);
}
