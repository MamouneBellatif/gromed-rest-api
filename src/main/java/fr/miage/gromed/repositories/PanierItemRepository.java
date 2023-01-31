package fr.miage.gromed.repositories;

import fr.miage.gromed.model.PanierItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PanierItemRepository extends JpaRepository<PanierItem, Long> {
    Optional<PanierItem> findByPanier_Id(Long id);

    Optional<PanierItem> findByPanier_IdAndPresentation_Id(Long idPanier, Long idProduit);

    boolean existsByPanier_IdAndPresentation_Id(Long idPanier, Long idProduit);
}