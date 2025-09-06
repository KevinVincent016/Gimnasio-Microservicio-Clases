package co.analisys.gimnasio.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

import co.analisys.gimnasio.dto.EntrenadorDTO;
import co.analisys.gimnasio.model.Clase;
import co.analisys.gimnasio.service.ClaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/classes")
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @Operation(
        summary = "Programar una nueva clase en el gimnasio",
        description = "Este endpoint permite crear y programar una nueva clase en el gimnasio. " +
        "Debes proporcionar el nombre, horario, capacidad máxima y el ID del entrenador asignado. " +
        "Retorna la información de la clase creada."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clase programada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    public Clase programarClase(@RequestBody Clase clase) {
        return claseService.programarClase(clase);
    }

    @Operation(
        summary = "Obtener todas las clases programadas",
        description = "Este endpoint recupera una lista completa de todas las clases que han sido programadas en el gimnasio. " +
        "No requiere parámetros y retorna los detalles de cada clase registrada."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clases recuperada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER', 'ROLE_MEMBER')")
    public List<Clase> obtenerTodasClases() {
        return claseService.obtenerTodasClases();
    }

    @Operation(
        summary = "Obtener una clase por ID",
        description = "Recupera los detalles de una clase específica utilizando su ID único. " +
        "Retorna la información de la clase si se encuentra."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clase recuperada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Clase no encontrada"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "404", description = "Clase no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER', 'ROLE_MEMBER')")
    public ResponseEntity<Clase> obtenerClasePorId(@PathVariable Long id) {
        Clase clase = claseService.obtenerClasePorId(id);
        if (clase != null) {
            return ResponseEntity.ok(clase);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Actualizar una clase existente",
        description = "Permite actualizar los detalles de una clase existente utilizando su ID. " +
        "Debes proporcionar los nuevos detalles de la clase en el cuerpo de la solicitud. " +
        "Retorna la información actualizada de la clase."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clase actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Clase no encontrada"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "404", description = "Clase no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    public ResponseEntity<Clase> actualizarClase(@PathVariable Long id, @RequestBody Clase clase) {
        Clase actualizada = claseService.actualizarClase(id, clase);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Eliminar una clase por ID",
        description = "Elimina una clase específica utilizando su ID único. " +
        "Retorna un estado de éxito si la clase fue eliminada correctamente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clase eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Clase no encontrada"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "404", description = "Clase no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER')")
    public ResponseEntity<Boolean> eliminarClase(@PathVariable Long id) {
        boolean eliminado = claseService.eliminarClase(id);
        if (eliminado) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Obtener el entrenador asignado a una clase",
        description = "Recupera los detalles del entrenador asignado a una clase específica utilizando el ID de la clase. " +
        "Retorna la información del entrenador si se encuentra."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entrenador recuperado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Clase o entrenador no encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Prohibido"),
        @ApiResponse(responseCode = "404", description = "Clase no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/trainer")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TRAINER', 'ROLE_MEMBER')")
    public ResponseEntity<Object> obtenerEntrenadorDeClase(@PathVariable Long id) {
        Clase clase = claseService.obtenerClasePorId(id);
        if (clase == null || clase.getEntrenadorId() == null) {
            return ResponseEntity.notFound().build();
        }
        EntrenadorDTO entrenador = claseService.obtenerEntrenadorDeClase(clase.getEntrenadorId());
        return ResponseEntity.ok(entrenador);
    }
}