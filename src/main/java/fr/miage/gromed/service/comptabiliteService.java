package fr.miage.gromed.service;

import fr.miage.gromed.model.ComptabiliteInterne;
import fr.miage.gromed.model.enums.ComptabiliteParametres;
import fr.miage.gromed.repositories.ComptabiliteInterneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
public class comptabiliteService {

    private final ComptabiliteInterneRepository comptabiliteInterneRepository;

    @Autowired
    public comptabiliteService(ComptabiliteInterneRepository comptabiliteInterneRepository) {
        this.comptabiliteInterneRepository = comptabiliteInterneRepository;
    }

    @Autowired
    public void populateComptabiliteInterne(){
        ComptabiliteParametres.getComptabiliteParameters().forEach(parameter -> {
            ComptabiliteInterne comptabiliteInterne = ComptabiliteInterne.builder()
                    .parametre(parameter)
                    .valeur(0.0)
                    .build();
            comptabiliteInterneRepository.save(comptabiliteInterne);
        });
    }

    @Transactional
    public void addVente(Double gain){
        ComptabiliteInterne comptabiliteInterne = comptabiliteInterneRepository.findByParametre(ComptabiliteParametres.CHIFFRE_AFFAIRE).orElseThrow(
                () -> new RuntimeException("Comptabilite interne not found")
        );
        comptabiliteInterne.setValeur(comptabiliteInterne.getValeur() + gain);
        comptabiliteInterneRepository.save(comptabiliteInterne);
    }
}
