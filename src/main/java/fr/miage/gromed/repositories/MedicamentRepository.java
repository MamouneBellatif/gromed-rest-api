package fr.miage.gromed.repositories;

import fr.miage.gromed.model.medicament.ComposantSubtance;
import fr.miage.gromed.model.medicament.Medicament;
import fr.miage.gromed.model.medicament.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
//    List<Medicament> findByComposantListLibe(List<ComposantSubtance> composantList);
//    List<Medicament> findByDenomination(String denomination);
}
