package fr.miage.gromed.model.medicament;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConditionPrescriptionRepository extends JpaRepository<ConditionPrescription, Long> {
    Optional<ConditionPrescription> findByLibelle(String libelle);
}