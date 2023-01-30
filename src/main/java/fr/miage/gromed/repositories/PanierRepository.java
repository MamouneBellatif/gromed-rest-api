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

    Panier findByItemsId(Long idItem);

//    List<Panier> findAllByDateExpirationAfter(LocalDateTime now);
    List<Panier> findAllByExpiresAtAfterAndExpired(LocalDateTime now, boolean expired);

    List<Panier> findByClientId(Long idUser);
}
