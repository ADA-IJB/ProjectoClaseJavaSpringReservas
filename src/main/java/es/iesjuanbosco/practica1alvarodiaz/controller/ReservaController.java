package es.iesjuanbosco.practica1alvarodiaz.controller;

import es.iesjuanbosco.practica1alvarodiaz.entity.FranjaHoraria;
import es.iesjuanbosco.practica1alvarodiaz.entity.Reserva;
import es.iesjuanbosco.practica1alvarodiaz.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    public ReservaController(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @GetMapping("/reservas")
    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    @GetMapping("/reservas/{id}")
    public ResponseEntity<Reserva> findById(@PathVariable Long id) {
        return reservaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id)
    {
        reservaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reservas/{id}")
    public ResponseEntity<Reserva> update(@PathVariable Long id, @RequestBody Reserva reserva) {
        if (!reservaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reserva.setId(id);

        if (
                reserva.getFecha().isBefore(LocalDate.now())
                        || comprobarFranjas(reserva)
        ) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(reservaRepository.save(reserva));
    }

    @PostMapping("/reservas")
    public ResponseEntity<Reserva> create(@RequestBody Reserva reserva) {
        if (
                reserva.getFecha().isBefore(LocalDate.now())
                || comprobarFranjas(reserva)
        ) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(reservaRepository.save(reserva));
    }

    public boolean comprobarFranjas(Reserva comprobar) {
        List<Reserva> reservas = reservaRepository.findAll();

        for (Reserva reserva : reservas) {
            if (reserva.getFecha().equals(comprobar.getFecha())) {
                if (reserva.getFranjaHoraria().equals(comprobar.getFranjaHoraria())) {
                    return true;
                }
            }
        }

        return false;
    }
}
