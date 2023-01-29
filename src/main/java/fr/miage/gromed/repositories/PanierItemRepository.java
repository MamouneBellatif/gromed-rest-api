package fr.miage.gromed.repositories;

import fr.miage.gromed.model.PanierItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PanierItemRepository extends JpaRepository<PanierItem, Long> {
}