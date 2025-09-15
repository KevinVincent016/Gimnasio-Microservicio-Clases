package co.analisys.gimnasio.service;

import org.springframework.stereotype.Service;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import co.analisys.gimnasio.repository.ClaseRepository;
import jakarta.transaction.Transactional;
import co.analisys.gimnasio.client.EntrenadorClient;
import co.analisys.gimnasio.dto.ClaseDTO;
import co.analisys.gimnasio.dto.EntrenadorDTO;
import co.analisys.gimnasio.model.Clase;

@Service
public class ClaseService {
    
    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private EntrenadorClient entrenadorClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

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

    public Clase actualizarClase(Long id, Clase claseActualizada) {
        return claseRepository.findById(id).map(clase -> {
            clase.setNombre(claseActualizada.getNombre());
            clase.setHorario(claseActualizada.getHorario());
            clase.setCapacidadMaxima(claseActualizada.getCapacidadMaxima());
            clase.setEntrenadorId(claseActualizada.getEntrenadorId());
            return claseRepository.save(clase);
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
