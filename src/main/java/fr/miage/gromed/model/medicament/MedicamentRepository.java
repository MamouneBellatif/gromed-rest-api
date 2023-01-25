package fr.miage.gromed.model.medicament;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MedicamentRepository extends PagingAndSortingRepository<Medicament, Long>, JpaSpecificationExecutor<Medicament> {
}