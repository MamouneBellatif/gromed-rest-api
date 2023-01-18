package fr.miage.GroMed.repositories;

import fr.miage.GroMed.model.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentRepository extends JpaRepository<Medicament, Long> {

}
