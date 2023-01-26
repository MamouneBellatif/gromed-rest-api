package fr.miage.gromed.repositories;

import fr.miage.gromed.model.medicament.Presentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PresentationRepository extends JpaRepository<Presentation, Long> {

    Page<Presentation>
    findByLibelleContainingIgnoreCaseOrMedicamentDenominationContainingIgnoreCase
            (String libelle,String denom,Pageable pageable);

    Page<Presentation> findByCodeCIP(Long cip, Pageable pageable);

    Optional<Presentation> findByCodeCIP(Long cip);
//    @Query("SELECT m FROM Medicament m WHERE m.codeCIS LIKE %:id%")
//    Page<Presentation> findByCodeCIPContaining(@Param("id") Integer id);
}