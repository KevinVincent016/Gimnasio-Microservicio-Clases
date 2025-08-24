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
}
