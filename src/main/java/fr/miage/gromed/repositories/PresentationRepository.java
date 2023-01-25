package fr.miage.gromed.repositories;

import fr.miage.gromed.model.medicament.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PresentationRepository extends JpaRepository<Presentation, Long> {
    
    Optional<Presentation> findByCodeCip(String codeCip);
}