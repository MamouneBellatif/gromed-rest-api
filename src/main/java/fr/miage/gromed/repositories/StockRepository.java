package fr.miage.gromed.repositories;

import fr.miage.gromed.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    //find stock with alarm flag to true
    List<Stock> findByRestockAlertFlagTrue();
}
