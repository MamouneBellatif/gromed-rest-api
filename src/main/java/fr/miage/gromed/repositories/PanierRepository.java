package fr.miage.gromed.repositories;

import fr.miage.gromed.model.Panier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface PanierRepository extends JpaRepository<Panier, Long> {
        List<Panier> findByDateCreationBefore(Timestamp dateCreation);

}
