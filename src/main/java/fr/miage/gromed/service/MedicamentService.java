package fr.miage.gromed.service;

import fr.miage.gromed.repositories.MedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicamentService {

    @Autowired
    private MedicamentRepository medicamentRepository;

    public int medicamentCount(){
        return medicamentRepository.findAll().size();
    }

}
