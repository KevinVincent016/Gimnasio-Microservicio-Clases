package co.analisys.gimnasio.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class RecuperacionService {
    
    private static final Logger logger = LoggerFactory.getLogger(RecuperacionService.class);
    
    @Autowired
    private KafkaConsumer<String, String> consumer;
    
    // Suponiendo que tienes un repositorio para guardar offsets
    // @Autowired
    // private OffsetRepository offsetRepository;
    
    private final AtomicBoolean running = new AtomicBoolean(false);
    
    @PostConstruct
    public void iniciarProcesamiento() {
        if (running.compareAndSet(false, true)) {
            new Thread(this::procesarMensajes).start();
        }
    }
    
    @PreDestroy
    public void detenerProcesamiento() {
        running.set(false);
        if (consumer != null) {
            consumer.close();
        }
    }
    
    private void procesarMensajes() {
        try {
            consumer.subscribe(Arrays.asList("ocupacion-clases", "datos-entrenamiento"));
            
            // Cargar último offset procesado desde una base de datos
            Map<TopicPartition, Long> ultimoOffsetProcesado = cargarUltimoOffset();
            
            if (!ultimoOffsetProcesado.isEmpty()) {
                for (Map.Entry<TopicPartition, Long> entry : ultimoOffsetProcesado.entrySet()) {
                    consumer.seek(entry.getKey(), entry.getValue());
                }
            }
            
            while (running.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                
                for (ConsumerRecord<String, String> record : records) {
                    procesarRecord(record);
                    guardarOffset(record.topic(), record.partition(), record.offset());
                }
            }
            
        } catch (Exception e) {
            logger.error("Error en el procesamiento de recuperación: {}", e.getMessage());
        }
    }
    
    private Map<TopicPartition, Long> cargarUltimoOffset() {
        // Implementar carga desde base de datos
        logger.info("Cargando últimos offsets procesados...");
        // return offsetRepository.findUltimosOffsets();
        return Map.of(); // Placeholder
    }
    
    private void procesarRecord(ConsumerRecord<String, String> record) {
        try {
            logger.info("Procesando record - Topic: {}, Partition: {}, Offset: {}", 
                       record.topic(), record.partition(), record.offset());
            
            // Procesar el mensaje según el topic
            switch (record.topic()) {
                case "ocupacion-clases":
                    procesarOcupacionClase(record);
                    break;
                case "datos-entrenamiento":
                    procesarDatosEntrenamiento(record);
                    break;
                default:
                    logger.warn("Topic desconocido: {}", record.topic());
            }
            
        } catch (Exception e) {
            logger.error("Error procesando record: {}", e.getMessage());
            throw e; // Re-lanzar para manejo de errores upstream
        }
    }
    
    private void procesarOcupacionClase(ConsumerRecord<String, String> record) {
        // Implementar procesamiento específico para ocupación de clases
        logger.info("Procesando ocupación de clase: {}", record.value());
    }
    
    private void procesarDatosEntrenamiento(ConsumerRecord<String, String> record) {
        // Implementar procesamiento específico para datos de entrenamiento
        logger.info("Procesando datos de entrenamiento: {}", record.value());
    }
    
    private void guardarOffset(String topic, int partition, long offset) {
        try {
            // Guardar el offset en una base de datos transaccional
            logger.debug("Guardando offset - Topic: {}, Partition: {}, Offset: {}", 
                        topic, partition, offset);
            
            // offsetRepository.guardarOffset(topic, partition, offset);
            
        } catch (Exception e) {
            logger.error("Error guardando offset: {}", e.getMessage());
        }
    }
}