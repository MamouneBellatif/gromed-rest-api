package fr.miage.gromed.repositories;

import fr.miage.gromed.model.medicament.ConditionPrescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConditionPrescriptionRepository extends JpaRepository<ConditionPrescription, Long> {
    Optional<ConditionPrescription> findByLibelle(String libelle);
}