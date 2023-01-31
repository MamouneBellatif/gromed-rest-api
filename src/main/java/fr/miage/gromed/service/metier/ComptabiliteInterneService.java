package fr.miage.gromed.service.metier;

import fr.miage.gromed.dto.Facture;
import fr.miage.gromed.model.ComptabiliteInterne;
import fr.miage.gromed.model.Panier;
import fr.miage.gromed.model.PanierItem;
import fr.miage.gromed.model.enums.ComptabiliteParametres;
import fr.miage.gromed.repositories.ComptabiliteInterneRepository;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.miage.gromed.model.enums.ComptabiliteParametres.CHIFFRE_AFFAIRE;

@Service
public class ComptabiliteInterneService {

        @Autowired
        private ComptabiliteInterneRepository comptabiliteInterneRepository;

        public void payerPanier() {
            // TODO
        }

      @Transactional
      @Lock(LockModeType.PESSIMISTIC_WRITE)
      public Facture createFacture(Panier panier) {
        // TODO générer la facture en dto pour génerer pdf en front
          Set<PanierItem> panierItems = panier.getItems();
          final var prixTotal = panier.getItems().stream().mapToDouble(item -> item.getPresentation().getPrixDeBase()+item.getPresentation().getHonoraireRemboursement()).sum();
          comptabiliteInterneRepository.findByParametre(CHIFFRE_AFFAIRE).ifPresentOrElse(comptabiliteInterne -> {
              comptabiliteInterne.setValeur(comptabiliteInterne.getValeur()+prixTotal);
              comptabiliteInterneRepository.save(comptabiliteInterne);
          }, () -> {
              final var comptabiliteInterne = new ComptabiliteInterne();
              comptabiliteInterne.setParametre(CHIFFRE_AFFAIRE);
              comptabiliteInterne.setValeur(prixTotal);
              comptabiliteInterneRepository.save(comptabiliteInterne);
          });
          Map<String, Integer> produitQuantite = panierItems.stream().collect(
                  Collectors.toMap(
                          panierItem -> panierItem.getPresentation().getLibelle() + " " + panierItem.getPresentation().getMedicament().getDenomination(),
                          PanierItem::getQuantite
                  )
          );

          Map<String, Double> coutProduit = panierItems.stream().collect(
                  Collectors.toMap(
                          panierItem -> panierItem.getPresentation().getLibelle() + " " + panierItem.getPresentation().getMedicament().getDenomination(),
                          panierItem -> panierItem.getPresentation().getPrixDeBase()+panierItem.getPresentation().getHonoraireRemboursement()
                  )
          );



        return Facture.builder().nomClient(panier.getClient().getNom()).montant(prixTotal).produitsAchetes(produitQuantite).prixProduits(coutProduit).date(LocalDateTime.now()).build();
    }

//    @Transactional
//    public void populateComptabiliteInterne(){
//        ComptabiliteParametres.getComptabiliteParameters().forEach(parameter -> {
//            ComptabiliteInterne comptabiliteInterne = ComptabiliteInterne.builder()
//                    .parametre(parameter)
//                    .valeur(0.0)
//                    .build();
//            comptabiliteInterneRepository.save(comptabiliteInterne);
//        });
//    }

}
