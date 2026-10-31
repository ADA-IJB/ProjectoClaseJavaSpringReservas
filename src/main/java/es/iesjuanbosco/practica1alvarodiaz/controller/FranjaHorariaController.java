package es.iesjuanbosco.practica1alvarodiaz.controller;

import es.iesjuanbosco.practica1alvarodiaz.entity.FranjaHoraria;
import es.iesjuanbosco.practica1alvarodiaz.repository.FranjaHorariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FranjaHorariaController {

    @Autowired
    private FranjaHorariaRepository franjaHorariaRepository;

    public FranjaHorariaController(FranjaHorariaRepository franjaHorariaRepository) {
        this.franjaHorariaRepository = franjaHorariaRepository;
    }

    @GetMapping("/franjasHorarias")
    public List<FranjaHoraria> findAll() {
        return franjaHorariaRepository.findAll();
    }

    @GetMapping("/franjasHorarias/{id}")
    public ResponseEntity<FranjaHoraria> findById(@PathVariable Long id) {
        return franjaHorariaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/franjasHorarias/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id)
    {
        franjaHorariaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/franjasHorarias/{id}")
    public ResponseEntity<FranjaHoraria> update(@PathVariable Long id, @RequestBody FranjaHoraria franjaHoraria) {
        if (!franjaHorariaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        franjaHoraria.setId(id);
        return ResponseEntity.ok(franjaHorariaRepository.save(franjaHoraria));
    }

    @PostMapping("/franjasHorarias")
    public ResponseEntity<FranjaHoraria> create(@RequestBody FranjaHoraria franjaHoraria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(franjaHorariaRepository.save(franjaHoraria));
    }
}
