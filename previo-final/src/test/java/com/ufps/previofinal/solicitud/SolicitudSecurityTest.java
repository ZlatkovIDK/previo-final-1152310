package com.ufps.previofinal.solicitud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufps.previofinal.usuario.Usuario;
import com.ufps.previofinal.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Módulo 5 — Pruebas de seguridad con @WithMockUser
 * Levanta el contexto completo de Spring + MockMvc.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class SolicitudSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Asegurar que el usuario "user1" exista en BD para la prueba 2
        if (usuarioRepository.findByUsername("user1").isEmpty()) {
            usuarioRepository.save(new Usuario("user1",
                passwordEncoder.encode("user123"), "ROLE_USER"));
        }
    }

    private String solicitudJson() throws Exception {
        SolicitudDTO dto = new SolicitudDTO();
        dto.setTipo(TipoSolicitud.SOPORTE);
        dto.setDescripcion("Prueba de seguridad");
        return objectMapper.writeValueAsString(dto);
    }

    // ─────────────────────────────────────────────────────────
    // Prueba 1: POST /api/solicitudes sin autenticación → 401 o 403
    // ─────────────────────────────────────────────────────────
    @Test
    void postSolicitud_sinAutenticacion_retorna401o403() throws Exception {
        mockMvc.perform(post("/api/solicitudes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(solicitudJson()))
               .andExpect(result -> {
                   int status = result.getResponse().getStatus();
                   assert status == 401 || status == 403
                       : "Se esperaba 401 o 403 pero fue: " + status;
               });
    }

    // ─────────────────────────────────────────────────────────
    // Prueba 2: POST /api/solicitudes con usuario autenticado (rol USER) → 201
    // ─────────────────────────────────────────────────────────
    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    void postSolicitud_usuarioAutenticado_retorna201() throws Exception {
        mockMvc.perform(post("/api/solicitudes")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(solicitudJson()))
               .andExpect(status().isCreated());
    }

    // ─────────────────────────────────────────────────────────
    // Prueba 3: PUT /api/solicitudes/{id}/aprobar con rol USER (sin ADMIN) → 403
    // El sistema rechaza en capa de seguridad ANTES de buscar el registro.
    // ID 999 puede no existir — debe retornar 403 de todas formas.
    // ─────────────────────────────────────────────────────────
    @Test
    @WithMockUser(username = "user1", roles = {"USER"})
    void aprobarSolicitud_sinRolAdmin_retorna403() throws Exception {
        mockMvc.perform(put("/api/solicitudes/999/aprobar")
                    .with(csrf())
                    .param("observacion", "intento no autorizado"))
               .andExpect(status().isForbidden());
    }
}
