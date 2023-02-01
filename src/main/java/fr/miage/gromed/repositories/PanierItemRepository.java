package fr.miage.gromed.repositories;

import fr.miage.gromed.model.Panier;
import fr.miage.gromed.model.PanierItem;
import fr.miage.gromed.model.medicament.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PanierItemRepository extends JpaRepository<PanierItem, Long> {
    Optional<PanierItem> findByPanier_Id(Long id);

    Optional<PanierItem> findByPanierIdAndPresentationId(Long idPanier, Long idProduit);

    Optional<PanierItem> findByPanierAndPresentation(Panier panier, Presentation presentation);

    boolean existsByPanierAndPresentation_Id(Panier panier, Long idProduit);

    boolean existsByPanier_IdAndPresentation_Id(Long idPanier, Long idProduit);
}