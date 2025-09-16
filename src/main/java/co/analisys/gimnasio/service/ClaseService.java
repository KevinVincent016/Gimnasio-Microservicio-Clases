package co.analisys.gimnasio.service;

import org.springframework.stereotype.Service;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import co.analisys.gimnasio.repository.ClaseRepository;
import jakarta.transaction.Transactional;
import co.analisys.gimnasio.client.EntrenadorClient;
import co.analisys.gimnasio.dto.EntrenadorDTO;
import co.analisys.gimnasio.model.Clase;
import co.analisys.gimnasio.events.ClaseHorarioCambioEvent;
import java.util.Collections;
import java.time.LocalDateTime;
import co.analisys.gimnasio.messaging.MessagingNames;

/**
 * Servicio para la gestión de clases, incluyendo la publicación de eventos de cambio de horario.
 */
@Service
public class ClaseService {
    
    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private EntrenadorClient entrenadorClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Programa una nueva clase y publica el evento correspondiente (actualmente no implementado para cambios de horario).
     */
    @Transactional
    public Clase programarClase(Clase clase) {
        
        //ClaseDTO claseDTO = new ClaseDTO(clase.getNombre(), clase.getHorario(), clase.getCapacidadMaxima(), clase.getEntrenadorId());
        
        rabbitTemplate.convertAndSend(
            "gimnasio.exchange", 
            "clases.events", 
            clase
        );

        return claseRepository.save(clase);
    }

    public List<Clase> obtenerTodasClases() {
        return claseRepository.findAll();
    }

    public Clase obtenerClasePorId(Long id) {
        return claseRepository.findById(id).orElse(null);
    }

    /**
     * Actualiza una clase existente. Si el horario cambia, publica un evento ClaseHorarioCambioEvent
     * al exchange de cambios de horario para notificar a miembros y entrenadores.
     */
    public Clase actualizarClase(Long id, Clase claseActualizada) {
        return claseRepository.findById(id).map(clase -> {
            boolean horarioCambio = !clase.getHorario().equals(claseActualizada.getHorario());
            LocalDateTime horarioAnterior = clase.getHorario();
            clase.setNombre(claseActualizada.getNombre());
            clase.setHorario(claseActualizada.getHorario());
            clase.setCapacidadMaxima(claseActualizada.getCapacidadMaxima());
            clase.setEntrenadorId(claseActualizada.getEntrenadorId());
            Clase updated = claseRepository.save(clase);
            if (horarioCambio) {
                ClaseHorarioCambioEvent event = new ClaseHorarioCambioEvent(
                    clase.getId(),
                    clase.getNombre(),
                    clase.getEntrenadorId(),
                    Collections.emptyList(), // Simulación: lista vacía de miembros
                    horarioAnterior,
                    clase.getHorario()
                );
                rabbitTemplate.convertAndSend(
                    MessagingNames.CLASES_HORARIO_EXCHANGE,
                    "",
                    event
                );
            }
            return updated;
        }).orElse(null);
    }

    public boolean eliminarClase(Long id) {
        if (claseRepository.existsById(id)) {
            claseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public EntrenadorDTO obtenerEntrenadorDeClase(Long entrenadorId) {
        return entrenadorClient.getEntrenadorById(entrenadorId);
    }
}
