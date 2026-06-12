package com.ufps.previofinal.mensaje;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufps.previofinal.usuario.Usuario;
import com.ufps.previofinal.usuario.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Módulo 4 — Pruebas de controlador con @WebMvcTest
 * Solo carga la capa web; no levanta contexto completo ni BD.
 */
@WebMvcTest(MensajeController.class)
class MensajeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MensajeService mensajeService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    // ─────────────────────────────────────────────────────────
    // Prueba 1: GET /api/mensajes/bandeja-entrada con usuario autenticado → 200
    // ─────────────────────────────────────────────────────────
    @Test
    @WithMockUser(username = "user1")
    void getBandejaEntrada_usuarioAutenticado_retorna200() throws Exception {
        Usuario usuarioMock = new Usuario("user1", "pass", "ROLE_USER");
        when(usuarioRepository.findByUsername("user1")).thenReturn(Optional.of(usuarioMock));
        when(mensajeService.getBandejaEntrada(any(Usuario.class))).thenReturn(List.of());

        mockMvc.perform(get("/api/mensajes/bandeja-entrada"))
               .andExpect(status().isOk());
    }

    // ─────────────────────────────────────────────────────────
    // Prueba 2: GET /api/mensajes/bandeja-entrada sin autenticación → 401 o 403
    // ─────────────────────────────────────────────────────────
    @Test
    void getBandejaEntrada_sinAutenticacion_retorna401o403() throws Exception {
        mockMvc.perform(get("/api/mensajes/bandeja-entrada"))
               .andExpect(result -> {
                   int status = result.getResponse().getStatus();
                   assert status == 401 || status == 403
                       : "Se esperaba 401 o 403 pero fue: " + status;
               });
    }

    // ─────────────────────────────────────────────────────────
    // Prueba 3: POST /api/mensajes con cuerpo vacío → 400 Bad Request
    // ─────────────────────────────────────────────────────────
    @Test
    @WithMockUser(username = "user1")
    void postMensaje_cuerpoVacio_retorna400() throws Exception {
        mockMvc.perform(post("/api/mensajes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
               .andExpect(status().isBadRequest());
    }
}
