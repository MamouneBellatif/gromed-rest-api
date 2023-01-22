package fr.miage.gromed.service;

import fr.miage.gromed.model.medicament.Medicament;
import fr.miage.gromed.repositories.MedicamentRepository;
import fr.miage.gromed.utils.DataWrapper;
import fr.miage.gromed.utils.MedicalDataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopulateService {

    @Autowired
    private MedicalDataParser medicalDataParser;

    @Autowired
    private MedicamentRepository medicamentRepository;

    public void populate() {
        medicalDataParser.initMedicament("src/main/resources/data/CIS_bdpm.txt");
        List<DataWrapper> list = medicalDataParser.parseMedicament();
        list.forEach(data -> {
            Medicament medicament = new Medicament();
            medicament.setCodeCIS(Integer.parseInt(data.data.get("CIS").toString()));
            medicament.setDenomination(data.data.get("denomination").toString());
            medicament.setFormePharmaceutique(data.data.get("forme_pharmaceutique").toString());
            medicament.setVoiesAdministration(data.data.get("voies_administration").toString());
            medicament.setStatutAdministratif(data.data.get("statut_admin_AMM").toString());
            System.out.println("populate: "+medicament.toString());
            System.out.println("###################");
            medicamentRepository.save(medicament);
        });

    }

}
