package fr.miage.gromed.service;

import fr.miage.gromed.repositories.MedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MedicamentService {

    @Autowired
    private MedicamentRepository medicamentRepository;

    public int medicamentCount(){
        return medicamentRepository.findAll().size();
    }

    /**
     * Retourne la liste des formes pharmaceutiques
     *
     * Parser une fois plutôt que de le faire à chaque fois ?
     * */
    public Set<String> getFormes(){
        return medicamentRepository.findAll().stream().map(medicament -> medicament.getFormePharmaceutique()).collect(Collectors.toSet());
    }

}
