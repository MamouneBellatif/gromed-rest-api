package fr.miage.gromed.repositories;

import fr.miage.gromed.model.Panier;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PanierRepository extends JpaRepository<Panier, Long> {
    Optional<Panier> findByIdPanier(long idPanier);
}
