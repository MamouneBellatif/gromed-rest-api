package fr.miage.gromed.repositories;

import fr.miage.gromed.model.medicament.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MedicamentRepository extends JpaRepository<Medicament, Long> {

        Optional<Medicament> findByCodeCIS(int codeCIS);
        List<Medicament> findAllByCodeCISIn(List<Integer> codeCISListe);
        List<Medicament> findByDenominationContainingIgnoreCaseOrFormePharmaceutiqueContainingIgnoreCase(String denomination, String formePharmaceutique);


//        @Query("SELECT m FROM Medicament m WHERE m.denomination LIKE %?1% OR m.formePharmaceutique LIKE %?2%")
}

