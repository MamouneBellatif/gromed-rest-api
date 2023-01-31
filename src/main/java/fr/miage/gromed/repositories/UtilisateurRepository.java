package fr.miage.gromed.repositories;

import fr.miage.gromed.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {
}