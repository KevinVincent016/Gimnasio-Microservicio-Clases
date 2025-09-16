package co.analisys.gimnasio.service.kafka;

import co.analisys.gimnasio.dto.kafka.OcupacionClase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OcupacionClaseConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(OcupacionClaseConsumer.class);
    
    @KafkaListener(topics = "ocupacion-clases", groupId = "monitoreo-grupo")
    public void consumirActualizacionOcupacion(OcupacionClase ocupacion) {
        try {
            // Actualizar dashboard en tiempo real
            actualizarDashboard(ocupacion);
            
            // Enviar alertas si la ocupación es alta
            if (ocupacion.getPorcentajeOcupacion() > 90) {
                enviarAlertaOcupacionAlta(ocupacion);
            }
            
            logger.info("Dashboard actualizado - Clase: {}, Ocupación: {}%", 
                       ocupacion.getNombreClase(), ocupacion.getPorcentajeOcupacion());
                       
        } catch (Exception e) {
            logger.error("Error procesando actualización de ocupación: {}", e.getMessage());
        }
    }
    
    private void actualizarDashboard(OcupacionClase ocupacion) {
        // Implementar lógica para actualizar dashboard en tiempo real
        // Esto podría incluir WebSockets, Server-Sent Events, etc.
        logger.info("Actualizando dashboard para clase: {}", ocupacion.getClaseId());
    }
    
    private void enviarAlertaOcupacionAlta(OcupacionClase ocupacion) {
        // Implementar lógica para enviar alertas cuando la ocupación sea alta
        logger.warn("ALERTA: Ocupación alta en clase {} - {}%", 
                   ocupacion.getNombreClase(), ocupacion.getPorcentajeOcupacion());
    }
}