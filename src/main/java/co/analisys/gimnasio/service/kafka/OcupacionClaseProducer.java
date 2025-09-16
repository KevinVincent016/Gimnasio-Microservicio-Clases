package co.analisys.gimnasio.service.kafka;

import co.analisys.gimnasio.dto.kafka.OcupacionClase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OcupacionClaseProducer {
    
    private static final Logger logger = LoggerFactory.getLogger(OcupacionClaseProducer.class);
    private static final String TOPIC = "ocupacion-clases";
    
    @Autowired
    private KafkaTemplate<String, OcupacionClase> kafkaTemplate;
    
    public void actualizarOcupacion(String claseId, String nombreClase, int ocupacionActual, int capacidadMaxima) {
        try {
            OcupacionClase ocupacion = new OcupacionClase(
                claseId, 
                nombreClase, 
                ocupacionActual, 
                capacidadMaxima, 
                LocalDateTime.now()
            );
            
            kafkaTemplate.send(TOPIC, claseId, ocupacion);
            logger.info("Actualización de ocupación enviada - Clase: {}, Ocupación: {}/{}", 
                       nombreClase, ocupacionActual, capacidadMaxima);
                       
        } catch (Exception e) {
            logger.error("Error enviando actualización de ocupación para clase {}: {}", claseId, e.getMessage());
        }
    }
}