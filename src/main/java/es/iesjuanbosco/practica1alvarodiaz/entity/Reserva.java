package es.iesjuanbosco.practica1alvarodiaz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    private LocalDateTime fechaInsercion;

    @ManyToOne
    Aula aula;

    @ManyToOne
    FranjaHoraria franjaHoraria;

    @ManyToOne
    Profesor profesor;

    // Hace que fechaInsercion coja la fecha del momento que se crea.
    @PrePersist
    protected void onCreate() {
        this.fechaInsercion = LocalDateTime.now();
    }
}
