package es.iesjuanbosco.practica1alvarodiaz.repository;

import es.iesjuanbosco.practica1alvarodiaz.entity.FranjaHoraria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranjaHorariaRepository extends JpaRepository<FranjaHoraria, Long> {
}
