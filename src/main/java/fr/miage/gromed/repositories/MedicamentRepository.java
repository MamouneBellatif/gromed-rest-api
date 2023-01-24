package fr.miage.gromed.repositories;

import fr.miage.gromed.model.medicament.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
//    List<Medicament> findByComposantListLibe(List<ComposantSubtance> composantList);
//    List<Medicament> findByDenomination(String denomination);
        Optional<Medicament> findByCodeCIS(int codeCIS);

        //custom query select * from medicament where codeCIS = ?1*

        @Query("select m from Medicament m where m.codeCIS = ?1")
        Optional<Medicament> findByCodeCISBis(int codeCIS);

}

