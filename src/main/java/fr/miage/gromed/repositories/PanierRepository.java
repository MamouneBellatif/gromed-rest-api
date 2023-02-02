package fr.miage.gromed.repositories;

import fr.miage.gromed.dto.PresentationDto;
import fr.miage.gromed.model.Panier;

import java.util.Optional;

import fr.miage.gromed.model.PanierItem;
import fr.miage.gromed.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface PanierRepository extends JpaRepository<Panier, Long> {
    List<Panier> findAllByExpiresAtAfterAndExpiredFalseOrderByDateCreationDesc(LocalDateTime expiresAt  );
    boolean existsByClientAndExpiresAtAfterAndPaidFalseAndCanceledFalse(Utilisateur utilisateur,LocalDateTime expiresAt);
    Optional<Panier> findByClientAndExpiresAtAfterAndPaidFalseAndCanceledFalse(Utilisateur utilisateur,LocalDateTime expiresAt);
    List<Panier> findByDateCreationAfter(LocalDateTime expirationTime);

    List<Panier> findByPaidTrueAndDeliveredFalseOrderByDatePaiement();

    Panier findByItemsId(Long idItem);

//    List<Panier> findAllByDateExpirationAfter(LocalDateTime now);
    List<Panier> findAllByExpiresAtBeforeAndExpired(LocalDateTime now, boolean expired);

    List<Panier> findByClientId(String string);

    List<Panier> findByClientAndExpiresAtBefore(Utilisateur utilisateur, LocalDateTime now);

    List<Panier> findByClient(Utilisateur utilisateur);

    @Query("select p from Panier p where p.client = ?1 and p.expiresAt > ?2")
    Optional<Panier> findByClientAndExpiresAtAfter(Utilisateur utilisateur, LocalDateTime now);
    Optional<Panier> findByClientAndExpiresAtAfterAndPaidFalseOrderByDateCreationDesc(Utilisateur utilisateur, LocalDateTime now);

    boolean existsByClientAndExpiresAtAfterAndPaidFalse(Utilisateur utilisateur, LocalDateTime time);

}
