package es.iesjuanbosco.practica1alvarodiaz.repository;

import es.iesjuanbosco.practica1alvarodiaz.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
}
