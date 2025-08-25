package co.analisys.gimnasio.service;

import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import co.analisys.gimnasio.repository.ClaseRepository;
import co.analisys.gimnasio.model.Clase;

@Service
public class ClaseService {
    
    @Autowired
    private ClaseRepository claseRepository;

    public Clase programarClase(Clase clase) {
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
}
