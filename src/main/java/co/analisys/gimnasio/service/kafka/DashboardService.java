package co.analisys.gimnasio.service.kafka;

import co.analisys.gimnasio.dto.kafka.OcupacionClase;
import co.analisys.gimnasio.dto.kafka.ResumenEntrenamiento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class DashboardService {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);
    
    // Almacenamiento en memoria para el dashboard (en producción usar Redis o base de datos)
    private final Map<String, OcupacionClase> ocupacionActual = new ConcurrentHashMap<>();
    private final Map<String, ResumenEntrenamiento> resumenMiembros = new ConcurrentHashMap<>();
    
    @KafkaListener(topics = "ocupacion-clases", groupId = "dashboard-grupo")
    public void actualizarDashboardOcupacion(OcupacionClase ocupacion) {
        try {
            ocupacionActual.put(ocupacion.getClaseId(), ocupacion);
            
            logger.info("Dashboard actualizado - Clase: {} ({}%), Timestamp: {}", 
                       ocupacion.getNombreClase(), 
                       String.format("%.1f", ocupacion.getPorcentajeOcupacion()),
                       ocupacion.getTimestamp());
            
            // Aquí podrías enviar via WebSocket a clientes conectados
            enviarActualizacionWebSocket(ocupacion);
            
        } catch (Exception e) {
            logger.error("Error actualizando dashboard de ocupación: {}", e.getMessage());
        }
    }
    
    @KafkaListener(topics = "resumen-entrenamiento", groupId = "dashboard-grupo")
    public void actualizarDashboardResumen(ResumenEntrenamiento resumen) {
        try {
            resumenMiembros.put(resumen.getMiembroId(), resumen);
            
            logger.info("Resumen actualizado - Miembro: {}, Sesiones: {}, Calorías: {}", 
                       resumen.getMiembroId(), 
                       resumen.getTotalSesiones(),
                       resumen.getTotalCalorias());
            
        } catch (Exception e) {
            logger.error("Error actualizando dashboard de resumen: {}", e.getMessage());
        }
    }
    
    public Map<String, OcupacionClase> getOcupacionActual() {
        return new ConcurrentHashMap<>(ocupacionActual);
    }
    
    public Map<String, ResumenEntrenamiento> getResumenMiembros() {
        return new ConcurrentHashMap<>(resumenMiembros);
    }
    
    public OcupacionClase getOcupacionClase(String claseId) {
        return ocupacionActual.get(claseId);
    }
    
    public ResumenEntrenamiento getResumenMiembro(String miembroId) {
        return resumenMiembros.get(miembroId);
    }
    
    private void enviarActualizacionWebSocket(OcupacionClase ocupacion) {
        // Implementar WebSocket para actualizaciones en tiempo real
        // Ejemplo: webSocketTemplate.convertAndSend("/topic/ocupacion", ocupacion);
        logger.debug("Enviando actualización WebSocket para clase: {}", ocupacion.getClaseId());
    }
}