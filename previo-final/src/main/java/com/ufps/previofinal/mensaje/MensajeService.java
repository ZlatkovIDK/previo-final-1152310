package com.ufps.previofinal.mensaje;

import com.ufps.previofinal.usuario.Usuario;
import com.ufps.previofinal.usuario.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final UsuarioRepository usuarioRepository;

    public MensajeService(MensajeRepository mensajeRepository,
                          UsuarioRepository usuarioRepository) {
        this.mensajeRepository = mensajeRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Mensaje enviar(MensajeDTO dto, Usuario emisor) {
        Usuario receptor = usuarioRepository.findByUsername(dto.getUsernameReceptor())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Usuario receptor no encontrado: " + dto.getUsernameReceptor()));

        Mensaje mensaje = new Mensaje();
        mensaje.setEmisor(emisor);
        mensaje.setReceptor(receptor);
        mensaje.setAsunto(dto.getAsunto());
        mensaje.setContenido(dto.getContenido());
        return mensajeRepository.save(mensaje);
    }

    public List<Mensaje> getBandejaEntrada(Usuario usuario) {
        return mensajeRepository.findByReceptorOrderByFechaEnvioDesc(usuario);
    }

    public List<Mensaje> getEnviados(Usuario usuario) {
        return mensajeRepository.findByEmisorOrderByFechaEnvioDesc(usuario);
    }

    public Mensaje marcarLeido(Long id, Usuario usuario) {
        Mensaje mensaje = mensajeRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Mensaje no encontrado con id: " + id));

        if (!mensaje.getReceptor().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "No puedes marcar mensajes de otros usuarios");
        }
        mensaje.setLeido(true);
        return mensajeRepository.save(mensaje);
    }

    public long contarNoLeidos(Usuario usuario) {
        return mensajeRepository.countByReceptorAndLeidoFalse(usuario);
    }
}
