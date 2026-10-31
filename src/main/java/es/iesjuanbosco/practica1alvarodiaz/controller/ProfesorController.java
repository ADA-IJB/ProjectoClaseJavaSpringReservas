package es.iesjuanbosco.practica1alvarodiaz.controller;

import es.iesjuanbosco.practica1alvarodiaz.entity.Profesor;
import es.iesjuanbosco.practica1alvarodiaz.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProfesorController {

    @Autowired
    private ProfesorRepository profesorRepository;

    public ProfesorController(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    @GetMapping("/profesores")
    public List<Profesor> findAll() {
        return profesorRepository.findAll();
    }

    @GetMapping("/profesores/{id}")
    public ResponseEntity<Profesor> findById(@PathVariable Long id) {
        return profesorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/profesores/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id)
    {
        profesorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profesores/{id}")
    public ResponseEntity<Profesor> update(@PathVariable Long id, @RequestBody Profesor profesor) {
        if (!profesorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        profesor.setId(id);
        return ResponseEntity.ok(profesorRepository.save(profesor));
    }

    @PostMapping("/profesores")
    public ResponseEntity<Profesor> create(@RequestBody Profesor profesor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profesorRepository.save(profesor));
    }
}
