package com.ufps.previofinal.solicitud;

import com.ufps.previofinal.usuario.Usuario;
import com.ufps.previofinal.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final UsuarioRepository usuarioRepository;

    public SolicitudController(SolicitudService solicitudService,
                                UsuarioRepository usuarioRepository) {
        this.solicitudService = solicitudService;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario getUsuarioActual(UserDetails userDetails) {
        return usuarioRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // 1. Radicar solicitud — POST /api/solicitudes → 201
    @PostMapping
    public ResponseEntity<Solicitud> radicar(
            @Valid @RequestBody SolicitudDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        Usuario solicitante = getUsuarioActual(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(solicitudService.radicar(dto, solicitante));
    }

    // 2. Mis solicitudes — GET /api/solicitudes/mis-solicitudes → 200
    @GetMapping("/mis-solicitudes")
    public ResponseEntity<List<Solicitud>> getMisSolicitudes(
            @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioActual(userDetails);
        return ResponseEntity.ok(solicitudService.getMisSolicitudes(usuario));
    }

    // 3. Todas las solicitudes — GET /api/solicitudes → 200 (solo ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Solicitud>> getTodas() {
        return ResponseEntity.ok(solicitudService.getTodas());
    }

    // 4. Aprobar — PUT /api/solicitudes/{id}/aprobar?observacion=texto → 200 (solo ADMIN)
    @PutMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Solicitud> aprobar(
            @PathVariable Long id,
            @RequestParam String observacion) {
        return ResponseEntity.ok(solicitudService.aprobar(id, observacion));
    }

    // 5. Rechazar — PUT /api/solicitudes/{id}/rechazar?observacion=texto → 200 (solo ADMIN)
    @PutMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Solicitud> rechazar(
            @PathVariable Long id,
            @RequestParam String observacion) {
        return ResponseEntity.ok(solicitudService.rechazar(id, observacion));
    }
}
