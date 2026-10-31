package es.iesjuanbosco.practica1alvarodiaz.repository;

import es.iesjuanbosco.practica1alvarodiaz.entity.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {
}
