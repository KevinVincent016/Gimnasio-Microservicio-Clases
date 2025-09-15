package co.analisys.gimnasio.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaseDTO implements Serializable {

    private Long id;
    private String nombre;
    private LocalDateTime horario;
    private int capacidadMaxima;
    private Long entrenadorId;
    
}
