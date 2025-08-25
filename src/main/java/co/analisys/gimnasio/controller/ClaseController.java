package co.analisys.gimnasio.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import co.analisys.gimnasio.model.Clase;
import co.analisys.gimnasio.service.ClaseService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/classes")
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping
    public Clase programarClase(@RequestBody Clase clase) {
        return claseService.programarClase(clase);
    }

    @GetMapping
    public List<Clase> obtenerTodasClases() {
        return claseService.obtenerTodasClases();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clase> obtenerClasePorId(@PathVariable Long id) {
        Clase clase = claseService.obtenerClasePorId(id);
        if (clase != null) {
            return ResponseEntity.ok(clase);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clase> actualizarClase(@PathVariable Long id, @RequestBody Clase clase) {
        Clase actualizada = claseService.actualizarClase(id, clase);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> eliminarClase(@PathVariable Long id) {
        boolean eliminado = claseService.eliminarClase(id);
        if (eliminado) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/trainer")
    public ResponseEntity<Object> obtenerEntrenadorDeClase(@PathVariable Long id) {
        Clase clase = claseService.obtenerClasePorId(id);
        if (clase == null || clase.getEntrenadorId() == null) {
            return ResponseEntity.notFound().build();
        }
        String url = "http://localhost:8081/api/trainers/" + clase.getEntrenadorId();
        Object entrenador = restTemplate.getForObject(url, Object.class);
        return ResponseEntity.ok(entrenador);
    }
}
