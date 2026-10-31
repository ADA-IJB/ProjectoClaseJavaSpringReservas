package es.iesjuanbosco.practica1alvarodiaz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FranjaHoraria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime horaInicio;
    private LocalTime horaFin;

    @JsonIgnore
    @OneToMany(mappedBy = "franjaHoraria", cascade = CascadeType.REMOVE)
    private List<Reserva> reserva;

//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        FranjaHoraria that = (FranjaHoraria) o;
//        return Objects.equals(id, that.id) && Objects.equals(horaInicio, that.horaInicio) && Objects.equals(horaFin, that.horaFin);
//    }

    // Para que se compruebe desde el frontend hay que descomentar esto y comentar el otro
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FranjaHoraria that = (FranjaHoraria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, horaInicio, horaFin, reserva);
    }

    @Override
    public String toString() {
        return "FranjaHoraria{" +
                "id=" + id +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", reserva=" + reserva +
                '}';
    }
}
