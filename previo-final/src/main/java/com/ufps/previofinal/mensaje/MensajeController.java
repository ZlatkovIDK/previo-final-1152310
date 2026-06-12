package com.ufps.previofinal.mensaje;

import com.ufps.previofinal.usuario.Usuario;
import com.ufps.previofinal.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeController {

    private final MensajeService mensajeService;
    private final UsuarioRepository usuarioRepository;

    public MensajeController(MensajeService mensajeService,
                              UsuarioRepository usuarioRepository) {
        this.mensajeService = mensajeService;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario getUsuarioActual(UserDetails userDetails) {
        return usuarioRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // 1. Enviar mensaje — POST /api/mensajes → 201
    @PostMapping
    public ResponseEntity<Mensaje> enviar(
            @Valid @RequestBody MensajeDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        Usuario emisor = getUsuarioActual(userDetails);
        Mensaje creado = mensajeService.enviar(dto, emisor);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // 2. Bandeja de entrada — GET /api/mensajes/bandeja-entrada → 200
    @GetMapping("/bandeja-entrada")
    public ResponseEntity<List<Mensaje>> getBandejaEntrada(
            @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioActual(userDetails);
        return ResponseEntity.ok(mensajeService.getBandejaEntrada(usuario));
    }

    // 3. Mensajes enviados — GET /api/mensajes/enviados → 200
    @GetMapping("/enviados")
    public ResponseEntity<List<Mensaje>> getEnviados(
            @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioActual(userDetails);
        return ResponseEntity.ok(mensajeService.getEnviados(usuario));
    }

    // 4. Marcar como leído — PUT /api/mensajes/{id}/leer → 200 o 404
    @PutMapping("/{id}/leer")
    public ResponseEntity<Mensaje> marcarLeido(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioActual(userDetails);
        return ResponseEntity.ok(mensajeService.marcarLeido(id, usuario));
    }

    // 5. Contar no leídos — GET /api/mensajes/no-leidos/count → {"count": N}
    @GetMapping("/no-leidos/count")
    public ResponseEntity<Map<String, Long>> contarNoLeidos(
            @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = getUsuarioActual(userDetails);
        long count = mensajeService.contarNoLeidos(usuario);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
