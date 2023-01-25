package fr.miage.gromed.repositories;

import fr.miage.gromed.model.PanierItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PanierElementRepository extends JpaRepository<PanierItem, Long> {

}

