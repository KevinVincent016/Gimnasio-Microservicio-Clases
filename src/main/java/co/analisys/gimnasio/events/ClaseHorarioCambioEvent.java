package co.analisys.gimnasio.events;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Evento que representa un cambio de horario en una clase.
 * Se utiliza para notificar a miembros y entrenadores sobre el cambio.
 */
public class ClaseHorarioCambioEvent implements Serializable {
    /** ID de la clase */
    private Long claseId;
    /** Nombre de la clase */
    private String nombreClase;
    /** ID del entrenador asignado */
    private Long entrenadorId;
    /** Lista de IDs de los miembros inscritos */
    private List<Long> miembrosIds;
    /** Horario anterior de la clase */
    private LocalDateTime horarioAnterior;
    /** Nuevo horario de la clase */
    private LocalDateTime nuevoHorario;

    public ClaseHorarioCambioEvent() {}

    public ClaseHorarioCambioEvent(Long claseId, String nombreClase, Long entrenadorId, List<Long> miembrosIds, LocalDateTime horarioAnterior, LocalDateTime nuevoHorario) {
        this.claseId = claseId;
        this.nombreClase = nombreClase;
        this.entrenadorId = entrenadorId;
        this.miembrosIds = miembrosIds;
        this.horarioAnterior = horarioAnterior;
        this.nuevoHorario = nuevoHorario;
    }

    public Long getClaseId() { return claseId; }
    public void setClaseId(Long claseId) { this.claseId = claseId; }

    public String getNombreClase() { return nombreClase; }
    public void setNombreClase(String nombreClase) { this.nombreClase = nombreClase; }

    public Long getEntrenadorId() { return entrenadorId; }
    public void setEntrenadorId(Long entrenadorId) { this.entrenadorId = entrenadorId; }

    public List<Long> getMiembrosIds() { return miembrosIds; }
    public void setMiembrosIds(List<Long> miembrosIds) { this.miembrosIds = miembrosIds; }

    public LocalDateTime getHorarioAnterior() { return horarioAnterior; }
    public void setHorarioAnterior(LocalDateTime horarioAnterior) { this.horarioAnterior = horarioAnterior; }

    public LocalDateTime getNuevoHorario() { return nuevoHorario; }
    public void setNuevoHorario(LocalDateTime nuevoHorario) { this.nuevoHorario = nuevoHorario; }

    @Override
    public String toString() {
        return "ClaseHorarioCambioEvent{" +
                "claseId=" + claseId +
                ", nombreClase='" + nombreClase + '\'' +
                ", entrenadorId=" + entrenadorId +
                ", miembrosIds=" + miembrosIds +
                ", horarioAnterior=" + horarioAnterior +
                ", nuevoHorario=" + nuevoHorario +
                '}';
    }
}
