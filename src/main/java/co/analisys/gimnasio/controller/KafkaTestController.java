package co.analisys.gimnasio.controller;

import co.analisys.gimnasio.service.kafka.OcupacionClaseProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka/test")
public class KafkaTestController {
    
    @Autowired
    private OcupacionClaseProducer ocupacionProducer;
    
    @PostMapping("/ocupacion")
    public ResponseEntity<String> testOcupacion(
            @RequestParam String claseId,
            @RequestParam String nombreClase,
            @RequestParam int ocupacionActual,
            @RequestParam int capacidadMaxima) {
        
        try {
            ocupacionProducer.actualizarOcupacion(claseId, nombreClase, ocupacionActual, capacidadMaxima);
            return ResponseEntity.ok("Mensaje de ocupación enviado correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error enviando mensaje: " + e.getMessage());
        }
    }
}