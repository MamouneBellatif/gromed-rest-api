package fr.miage.gromed.repositories;

import fr.miage.gromed.model.ComptabiliteInterne;
import fr.miage.gromed.model.enums.ComptabiliteParametres;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComptabiliteInterneRepository extends JpaRepository<ComptabiliteInterne, Long> {

    Optional<ComptabiliteInterne> findByParametre(ComptabiliteParametres chiffreAffaire);
}