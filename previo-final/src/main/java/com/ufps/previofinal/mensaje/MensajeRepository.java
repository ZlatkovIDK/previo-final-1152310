package com.ufps.previofinal.mensaje;

import com.ufps.previofinal.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByReceptorOrderByFechaEnvioDesc(Usuario receptor);
    List<Mensaje> findByEmisorOrderByFechaEnvioDesc(Usuario emisor);
    long countByReceptorAndLeidoFalse(Usuario receptor);
}
