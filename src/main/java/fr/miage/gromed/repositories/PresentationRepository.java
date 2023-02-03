package fr.miage.gromed.repositories;

import fr.miage.gromed.model.medicament.Presentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PresentationRepository extends JpaRepository<Presentation, Long> {
    @Query("select p from Presentation p where   CAST( p.codeCIP AS string ) like concat('%', ?1, '%') or   CAST( p.codeCIP AS string ) like concat('%', ?2, '%')  or  CAST( p.codeCIP AS string ) like concat('%', ?3, '%') or  CAST( p.codeCIP AS string ) like concat('%', ?4, '%') or  CAST( p.codeCIP AS string ) like concat('%', ?5, '%') ")
    Page<Presentation> findByCodeCIPIn(String codeCIP, String codeCIP2, String codeCIP3, String codeCIP4, String codeCIP5,
                                       Pageable pageable);

    @Query("select count(distinct p) from Presentation p where p.id = ?1")
    long countPresentation(Long id);

//    @Query("""
//            select p from Presentation p
//            where upper(p.libelle) like upper(concat('%', ?1, '%')) or upper(p.medicament.denomination) like upper(concat('%', ?2, '%')) and upper(p.prixDeBase) is not null and upper(p.prixDeBase) > ?3""")
//    Page<Presentation>
//    findByLibelleContainingIgnoreCaseOrMedicamentDenominationContainingIgnoreCaseAndPrixDeBaseNotNullAndPrixDeBaseGreaterThanAllIgnoreCase
//            (String libelle, String denom, Pageable pageable, double prix);

    @Query("""
            select p from Presentation p
            where upper(p.libelle) like upper(concat('%', ?1, '%')) or upper(p.medicament.denomination) like upper(concat('%', ?1, '%')) or CAST( p.codeCIP AS string ) like concat('%', ?1, '%') and p.prixDeBase is not null and p.prixDeBase > ?2""")
    Page<Presentation> searchQuery(String libelle, double minPrix, Pageable pageable);

    Page<Presentation> findByCodeCIP(Long cip, Pageable pageable);

    Optional<Presentation> findByCodeCIP(Long cip);
    @Query("select count(p) from Presentation p")
    long countAll();

//    @Query("select p from Presentation p where p.codeCIP = :codeCIP or p.medicament.codeCIS = :codeCIS")
//    List<Presentation> findByCodeCIPOrMedicament_CodeCIS(@Param("codeCIP") Long codeCIP, @Param("codeCIS") int codeCIS, Pageable pageable);
//



//    @Query("SELECT m FROM Medicament m WHERE m.codeCIS LIKE %:id%")
//    Page<Presentation> findByCodeCIPContaining(@Param("id") Integer id);

//    getFiveRandomPresentations();

}