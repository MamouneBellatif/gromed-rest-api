package fr.miage.gromed.repositories;

import fr.miage.gromed.model.Panier;

import java.util.Optional;

import fr.miage.gromed.model.PanierItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface PanierRepository extends JpaRepository<Panier, Long> {
    List<Panier> findByDateCreationAfter(LocalDateTime expirationTime);

    Optional<Panier> findById(Long idPanier);

    Panier findByItemsId(Long idItem);
}
