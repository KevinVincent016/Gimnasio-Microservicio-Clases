package co.analisys.gimnasio.service;

import co.analisys.gimnasio.events.ClaseHorarioCambioEvent;
import co.analisys.gimnasio.messaging.MessagingNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ClaseHorarioCambioListener {
    private static final Logger log = LoggerFactory.getLogger(ClaseHorarioCambioListener.class);

    /**
     * Listener para miembros.
     */
    @RabbitListener(queues = MessagingNames.CLASES_HORARIO_MIEMBROS_QUEUE)
    public void recibirCambioHorarioMiembros(ClaseHorarioCambioEvent event) {
        log.info("[MIEMBROS] Recibido evento de cambio de horario: {}", event);
        // Aquí iría la lógica de notificación a miembros
    }

    /**
     * Listener para entrenadores.
     */
    @RabbitListener(queues = MessagingNames.CLASES_HORARIO_ENTRENADORES_QUEUE)
    public void recibirCambioHorarioEntrenadores(ClaseHorarioCambioEvent event) {
        log.info("[ENTRENADORES] Recibido evento de cambio de horario: {}", event);
        // Aquí iría la lógica de notificación a entrenadores
    }
}
