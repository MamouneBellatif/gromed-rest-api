package fr.miage.gromed.repositories;

import fr.miage.gromed.dto.CommandeTypeDto;
import fr.miage.gromed.model.CommandeType;
import fr.miage.gromed.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CommandeTypeRepository extends JpaRepository<CommandeType, Long> {
    List<CommandeType> findByPanier_Client(Utilisateur client);

    List<CommandeType> findAllByPanierClient(Utilisateur client);
}