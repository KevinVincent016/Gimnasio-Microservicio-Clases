package co.analisys.gimnasio.controller;

import co.analisys.gimnasio.dto.kafka.OcupacionClase;
import co.analisys.gimnasio.dto.kafka.ResumenEntrenamiento;
import co.analisys.gimnasio.service.kafka.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    @GetMapping("/ocupacion")
    public ResponseEntity<Map<String, OcupacionClase>> getOcupacionGeneral() {
        return ResponseEntity.ok(dashboardService.getOcupacionActual());
    }
    
    @GetMapping("/ocupacion/{claseId}")
    public ResponseEntity<OcupacionClase> getOcupacionClase(@PathVariable String claseId) {
        OcupacionClase ocupacion = dashboardService.getOcupacionClase(claseId);
        if (ocupacion != null) {
            return ResponseEntity.ok(ocupacion);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, ResumenEntrenamiento>> getResumenGeneral() {
        return ResponseEntity.ok(dashboardService.getResumenMiembros());
    }
    
    @GetMapping("/resumen/{miembroId}")
    public ResponseEntity<ResumenEntrenamiento> getResumenMiembro(@PathVariable String miembroId) {
        ResumenEntrenamiento resumen = dashboardService.getResumenMiembro(miembroId);
        if (resumen != null) {
            return ResponseEntity.ok(resumen);
        }
        return ResponseEntity.notFound().build();
    }
}