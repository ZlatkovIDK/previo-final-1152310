package com.ufps.previofinal.config;

import com.ufps.previofinal.usuario.Usuario;
import com.ufps.previofinal.usuario.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(new Usuario("admin",
                    passwordEncoder.encode("admin123"), "ROLE_ADMIN"));
                usuarioRepository.save(new Usuario("user1",
                    passwordEncoder.encode("user123"), "ROLE_USER"));
                usuarioRepository.save(new Usuario("user2",
                    passwordEncoder.encode("user123"), "ROLE_USER"));
                System.out.println("=== Usuarios de prueba creados ===");
                System.out.println("  admin / admin123  (rol: ADMIN)");
                System.out.println("  user1 / user123   (rol: USER)");
                System.out.println("  user2 / user123   (rol: USER)");
            }
        };
    }
}
