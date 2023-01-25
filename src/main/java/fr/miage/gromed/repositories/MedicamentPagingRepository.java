package fr.miage.gromed.repositories;

import fr.miage.gromed.model.medicament.Medicament;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentPagingRepository extends PagingAndSortingRepository<Medicament, Long> {



}
