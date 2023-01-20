package fr.miage.gromed.repositories;

import fr.miage.gromed.model.medicament.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByPresentationId(Long presentationId);

    //find stock with alarm flag to true
    List<Stock> findByRestockAlertFlagTrue();
}
