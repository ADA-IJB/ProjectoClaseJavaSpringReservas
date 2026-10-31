package es.iesjuanbosco.practica1alvarodiaz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer numeroClase;

    @Column
    private Integer capacidad;

    @Column
    private Boolean tieneOrdenadores;

    @JsonIgnore
    @OneToMany(mappedBy = "aula", cascade = CascadeType.REMOVE)
    private List<Reserva> reservas;
}
