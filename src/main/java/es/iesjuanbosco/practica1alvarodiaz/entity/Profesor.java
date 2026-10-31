package es.iesjuanbosco.practica1alvarodiaz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(length = 100, nullable = false)
    private String apellidos;

    @Column(length = 10, nullable = false)
    private String dni;

    private LocalDate fechaNacimiento;

    private LocalDate fechaIncorporacion;

    @Column(precision = 7, scale = 3)
    private BigDecimal sueldo;


    @JsonIgnore
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.REMOVE)
    private List<Reserva> reservas;
}
