package com.ufps.previofinal.solicitud;

import com.ufps.previofinal.usuario.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;

    public SolicitudService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    // 1. Radicar solicitud (estado automático PENDIENTE)
    public Solicitud radicar(SolicitudDTO dto, Usuario solicitante) {
        Solicitud s = new Solicitud();
        s.setSolicitante(solicitante);
        s.setTipo(dto.getTipo());
        s.setDescripcion(dto.getDescripcion());
        s.setEstado(EstadoSolicitud.PENDIENTE);
        return solicitudRepository.save(s);
    }

    // 2. Mis solicitudes (solo del usuario actual)
    public List<Solicitud> getMisSolicitudes(Usuario usuario) {
        return solicitudRepository.findBySolicitanteOrderByFechaCreacionDesc(usuario);
    }

    // 3. Todas (solo ADMIN)
    public List<Solicitud> getTodas() {
        return solicitudRepository.findAllByOrderByFechaCreacionDesc();
    }

    // 4. Aprobar (solo ADMIN)
    public Solicitud aprobar(Long id, String observacion) {
        Solicitud s = solicitudRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Solicitud no encontrada con id: " + id));
        s.setEstado(EstadoSolicitud.APROBADA);
        s.setObservacion(observacion);
        s.setFechaResolucion(LocalDateTime.now());
        return solicitudRepository.save(s);
    }

    // 5. Rechazar (solo ADMIN)
    public Solicitud rechazar(Long id, String observacion) {
        Solicitud s = solicitudRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Solicitud no encontrada con id: " + id));
        s.setEstado(EstadoSolicitud.RECHAZADA);
        s.setObservacion(observacion);
        s.setFechaResolucion(LocalDateTime.now());
        return solicitudRepository.save(s);
    }

    // Estadísticas para panel
    public long contarTotal()     { return solicitudRepository.count(); }
    public long contarPendientes(){ return solicitudRepository.countByEstado(EstadoSolicitud.PENDIENTE); }
    public long contarAprobadas() { return solicitudRepository.countByEstado(EstadoSolicitud.APROBADA); }
    public long contarRechazadas(){ return solicitudRepository.countByEstado(EstadoSolicitud.RECHAZADA); }
}
