package com.ufps.previofinal.solicitud;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/solicitudes")
@PreAuthorize("hasRole('ADMIN')")
public class PanelAdminController {

    private final SolicitudService solicitudService;

    public PanelAdminController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping("/panel")
    public String panel(Model model) {
        model.addAttribute("total",      solicitudService.contarTotal());
        model.addAttribute("pendientes", solicitudService.contarPendientes());
        model.addAttribute("aprobadas",  solicitudService.contarAprobadas());
        model.addAttribute("rechazadas", solicitudService.contarRechazadas());
        model.addAttribute("solicitudes",solicitudService.getTodas());
        return "admin/panel-solicitudes";
    }
}
