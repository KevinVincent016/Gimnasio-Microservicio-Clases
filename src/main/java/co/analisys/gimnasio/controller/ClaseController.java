package co.analisys.gimnasio.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import co.analisys.gimnasio.model.Clase;
import co.analisys.gimnasio.service.ClaseService;

@RestController
@RequestMapping("/api/gimnasio/clases")
public class ClaseController {
    
    @Autowired
    private ClaseService claseService;

    @PostMapping("/clases")
    public Clase programarClase(@RequestBody Clase clase) {
        return claseService.programarClase(clase);
    }

    @GetMapping("/clases")
    public List<Clase> obtenerTodasClases() {
        return claseService.obtenerTodasClases();
    }
    
}
