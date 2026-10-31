package es.iesjuanbosco.practica1alvarodiaz.controller;

import es.iesjuanbosco.practica1alvarodiaz.entity.Aula;
import es.iesjuanbosco.practica1alvarodiaz.repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AulaController {

    @Autowired
    private AulaRepository aulaRepository;

    public AulaController(AulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    @GetMapping("/aulas")
    public List<Aula> findAll(
            @RequestParam(defaultValue = "-1") int capacidad,
            @RequestParam(required = false) Boolean tieneOrdenadores
    ) {
        List<Aula> aulas = aulaRepository.findAll();
        List<Aula> aulasBien = new ArrayList<>();

        for (Aula aula : aulas) {
            if (aula.getCapacidad() > capacidad) {
                if (tieneOrdenadores == null || aula.getTieneOrdenadores() == tieneOrdenadores) {
                    aulasBien.add(aula);
                }
            }
        }

        return aulasBien;
    }

    @GetMapping("/aulas/{id}")
    public ResponseEntity<Aula> findById(@PathVariable Long id) {
        return aulaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/aulas/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id)
    {
        aulaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/aulas/{id}")
    public ResponseEntity<Aula> update(@PathVariable Long id, @RequestBody Aula aula) {
        if (!aulaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        aula.setId(id);
        return ResponseEntity.ok(aulaRepository.save(aula));
    }

    @PostMapping("/aulas")
    public ResponseEntity<Aula> create(@RequestBody Aula aula) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaRepository.save(aula));
    }
}
