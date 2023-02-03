package fr.miage.gromed.service.metier;

import fr.miage.gromed.dto.Facture;
import fr.miage.gromed.dto.ItemFacture;
import fr.miage.gromed.dto.PanierItemDto;
import fr.miage.gromed.exceptions.PresentationNotFoundException;
import fr.miage.gromed.model.ComptabiliteInterne;
import fr.miage.gromed.model.Panier;
import fr.miage.gromed.model.PanierItem;
import fr.miage.gromed.model.enums.ComptabiliteParametres;
import fr.miage.gromed.repositories.ComptabiliteInterneRepository;
import fr.miage.gromed.repositories.PanierRepository;
import fr.miage.gromed.repositories.PresentationRepository;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static fr.miage.gromed.model.enums.ComptabiliteParametres.CHIFFRE_AFFAIRE;
import static fr.miage.gromed.model.enums.ComptabiliteParametres.TRESORERIE;

@Service
public class ComptabiliteInterneService {

    private final ComptabiliteInterneRepository comptabiliteInterneRepository;
    private final PresentationRepository presentationRepository;

    @Autowired
    public ComptabiliteInterneService(ComptabiliteInterneRepository comptabiliteInterneRepository,
                                      PresentationRepository presentationRepository,
                                      PanierRepository panierRepository) {
        this.comptabiliteInterneRepository = comptabiliteInterneRepository;
        this.presentationRepository = presentationRepository;
    }

    public void payerPanier() {
            // TODO
        }

      @Transactional(rollbackFor = Exception.class)
      @Lock(LockModeType.PESSIMISTIC_WRITE)
      public Facture createFacture(Panier panier) {
          var prixTTC = getPrixTotal(panier);
          this.cashTransaction(CHIFFRE_AFFAIRE,prixTTC);

          return this.generateFacture(panier);
//          Map<String, Integer> produitQuantite = panierItems.stream().collect(
//                  Collectors.toMap(
//                          panierItem -> panierItem.getPresentation().getLibelle() + ": " + panierItem.getPresentation().getMedicament().getDenomination(),
//                          PanierItem::getQuantite
//                  )
//          );
//          Map<String, Double> coutProduit = panierItems.stream().collect(
//                  Collectors.toMap(
//                          panierItem -> panierItem.getPresentation().getLibelle() + ": " + panierItem.getPresentation().getMedicament().getDenomination(),
//                          panierItem -> panierItem.getPresentation().getPrixDeBase()+panierItem.getPresentation().getHonoraireRemboursement()
//                  )
//          );

//          Map<Object,Object> produitQuantite = panierItems.stream().collect(
//                  Collectors.toMap(
//                          panierItem -> panierItem.getPresentation().getLibelle() + ": " + panierItem.getPresentation().getMedicament().getDenomination(),
//                          panierItem -> Map.of(panierItem.getQuantite(),panierItem.getPresentation().getPrixDeBase()+panierItem.getPresentation().getHonoraireRemboursement())
//                  )
//          );
//        return Facture.builder().idPanier(panier.getId()).nomClient(panier.getClient().getNom()).montant(prixTTC).produitsAchetes(produitQuantite).date(LocalDateTime.now()).build();
    }

    public Facture generateFacture(Panier panier) {
        var prixTTC = getPrixTotal(panier);
        Set<PanierItem> panierItems = panier.getItems();
        List<ItemFacture> itemsFacture = panierItems.stream().map(item -> {
            return ItemFacture.builder().libelle(item.getPresentation().getLibelle()+ ": " + item.getPresentation().getMedicament().getDenomination()).quantite(item.getQuantite()).prixUnitaire(item.getPresentation().getPrixDeBase()+item.getPresentation().getHonoraireRemboursement()).build();
        }).collect(Collectors.toList());
        return Facture.builder().idPanier(panier.getId()).nomClient(panier.getClient().getNom()).montant(prixTTC).produitsAchetes(itemsFacture).date(LocalDateTime.now()).build();
    }

    private double getPrixTotal(Panier panier) {
        Set<PanierItem> panierItems = panier.getItems();
        final var tva = 0.2;
        var prixTotal = panierItems.stream().mapToDouble(item -> (item.getPresentation().getPrixDeBase()+item.getPresentation().getHonoraireRemboursement())* item.getQuantite()).sum();
        return  prixTotal + prixTotal*tva;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void payProductReStock(List<PanierItemDto> panierItemDto){
        var totalCharge = panierItemDto.stream().mapToDouble(item -> {
            var presentation = presentationRepository.findByCodeCIP(item.getPresentationCip()).orElseThrow(PresentationNotFoundException::new);
            return presentation.getPrixDeBase()*item.getQuantite();
        }).sum();
        this.cashTransaction(ComptabiliteParametres.CHARGES,totalCharge);
    }

    @Transactional(rollbackFor = Exception.class)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void cashTransaction(ComptabiliteParametres param, double prixTTC){
        comptabiliteInterneRepository.findByParametre(param).ifPresentOrElse(comptabiliteInterne -> {
            comptabiliteInterne.setValeur(comptabiliteInterne.getValeur()+prixTTC);
            comptabiliteInterneRepository.save(comptabiliteInterne);
        }, () -> initComptabiliteParametre(param,prixTTC));
    }

    @Transactional(rollbackFor = Exception.class)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void initComptabiliteParametre(ComptabiliteParametres parametre, double value){
        ComptabiliteInterne comptabiliteInterne = ComptabiliteInterne.builder()
                .parametre(parametre)
                .valeur(value)
                .build();
        comptabiliteInterneRepository.save(comptabiliteInterne);
    }


    @Transactional(rollbackFor = Exception.class)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
            public void reimburse(PanierItem panierItem)
            {
                double prixTTC = panierItem.getPresentation().getPrixDeBase()+panierItem.getPresentation().getHonoraireRemboursement();
                this.cashTransaction(TRESORERIE,-prixTTC);
            }
    }
