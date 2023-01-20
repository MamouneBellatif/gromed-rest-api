package fr.miage.gromed.repositories;

import fr.miage.gromed.model.medicament.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PresentationRepository extends JpaRepository<Presentation, Long> {

    Collection<Presentation> findByMedicamentId(Long medicamentId);
//    Collection<Presentation> findByMedicamentI(Long medicamentId);
}