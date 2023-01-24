package fr.miage.gromed.service;

import fr.miage.gromed.model.Stock;
import fr.miage.gromed.model.enums.NatureComposant;
import fr.miage.gromed.model.enums.TypeAvis;
import fr.miage.gromed.model.enums.ValeurAvis;
import fr.miage.gromed.model.medicament.*;
import fr.miage.gromed.repositories.MedicamentRepository;
import fr.miage.gromed.repositories.StockRepository;
import fr.miage.gromed.utils.DataWrapper;
import fr.miage.gromed.utils.MedicalDataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PopulateService {

    @Autowired
    private MedicalDataParser medicalDataParser;

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private ConditionPrescriptionRepository conditionPrescriptionRepository;

    private Map<Integer, String> medicamentLaboMap = new HashMap<>();
    public void populateMedicament() {
        medicalDataParser.initMedicament("src/main/resources/data/CIS_bdpm.txt");
        List<DataWrapper> list = medicalDataParser.parseMedicament();
        list.forEach(data -> {
            System.out.println(data.data.get("CIS"));
            Medicament medicament = Medicament.builder()
                            .codeCIS(Integer.parseInt(data.data.get("CIS")))
                            .denomination(data.data.get("denomination"))
                            .formePharmaceutique(data.data.get("forme_pharmaceutique"))
                            .voiesAdministration(data.data.get("voies_administration"))
                            .statutAdministratif(data.data.get("statut_admin_AMM"))
                            .dateAMM(MedicalDataParser.strToDate(data.data.get("date_AMM"), false))
                            .isSurveillanceRenforcee(Boolean.parseBoolean(data.data.get("surveillance_renforcee")))
                            .typeProcedureAMM(data.data.get("type_procedure_AMM"))
                            .statutBDM(data.data.get("statut_BDM"))
                            .numeroAutorisationEuro(data.data.get("numero_autorisation_Europeenne")).build();
            medicamentLaboMap.put(medicament.getCodeCIS(), data.data.get("titulaires"));

            System.out.println("populateMedicament: "+medicament.toString());
            System.out.println("###################");
            medicamentRepository.save(medicament);
        });
    }
        //use medicalDataParser to parse CIS_CIP_bdpm.txt and save the result in the database accordingly to the model using medicamentRepository CIS as foreign key
     public void populatePresentation() {
         medicalDataParser.initPresentation("src/main/resources/data/CIS_CIP_bdpm.txt");
         List<DataWrapper> list = medicalDataParser.parsePresentation();
         list.forEach(data -> {
             Optional<Medicament> medicamentOpt;
             medicamentOpt = medicamentRepository.findByCodeCIS(
                     Integer.parseInt(data.data.get("CIS")));
                if (medicamentOpt.isPresent()) {
                    Presentation presentation = Presentation.builder().codeCIP(Integer.parseInt(data.data.get("CIP13")))
                            .libelle(data.data.get("libelle"))
                            .prixDeBase(Double.parseDouble(data.data.get("prix_base")))
                            .honoraireRemboursement(Double.parseDouble(data.data.get("honoraire")))
                            .tauxRemboursement(data.data.get("taux_remboursement"))
                            .isAgrement(Boolean.parseBoolean(data.data.get("agrement_collectivites")))
                            .dateDeclaration(MedicalDataParser.strToDate(data.data.get("date_declaration_commercialisation"), false))
                            .etatCommercialisation(data.data.get("etat_commercialisation"))
                            .statutAdmin(data.data.get("statut_admin"))
                            .build();
                    Medicament medicament = medicamentOpt.get();
                    medicament.addPresentation(presentation);
                    presentation.setStock(this.generateStock(presentation));
                    medicamentRepository.save(medicament);
                }
         });
     }
     private Stock generateStock(Presentation presentation){
             int stockValue = (int) (Math.random() * 1000) + 100;
            return Stock.builder().quantiteStockPhysique(stockValue)
            .quantiteStockLogique(stockValue)
            .restockAlertFlag(false)
            .presentation(presentation).build();
     }

     public void populateComposant() {
         medicalDataParser.initComposants("src/main/resources/data/CIS_COMPO_bdpm.txt");
         List<DataWrapper> list = medicalDataParser.parseComposant();
         list.forEach(data -> {
             Optional<Medicament> medicamentOpt;
             medicamentOpt = medicamentRepository.findByCodeCIS(
                     Integer.parseInt(data.data.get("CIS")));
             if (medicamentOpt.isPresent()) {
                 ComposantSubtance composantSubtance = ComposantSubtance.builder()
                         .codeSubstance(Integer.parseInt(data.data.get("code_substance")))
                         .denomination(data.data.get("denomination"))
                         .designationElementPharmaceutique(data.data.get("designation_element_pharmaceutique"))
                         .natureComposant(NatureComposant.fromString(data.data.get("nature_composant")))
                         .dosage(data.data.get("dosage_substance"))
                         .referenceDosage(data.data.get("reference_dosage"))
                         .build();
                    Medicament medicament = medicamentOpt.get();
                    medicament.addComposant(composantSubtance);
                    medicamentRepository.save(medicament);
             }
         });
     }

     public void populateAvis(){
        medicalDataParser.initAvisASMR("src/main/resources/data/CIS_HAS_ASMR_bdpm.txt");
        medicalDataParser.initAvisSMR("src/main/resources/data/CIS_HAS_SMR_bdpm.txt");

        List<DataWrapper> resultAsmr = medicalDataParser.parseASMR();
        resultAsmr.forEach(data -> data.data.put("type_avis", "ASMR"));
        List<DataWrapper> resultSmr = medicalDataParser.parseSMR();
        resultSmr.forEach(data -> data.data.put("type_avis", "SMR"));
        resultAsmr.addAll(resultSmr);
        Stream.concat(resultSmr.stream(), resultAsmr.stream()).forEach(data -> {
             Optional<Medicament> medicamentOpt;
             medicamentOpt = medicamentRepository.findByCodeCIS(
                     Integer.parseInt(data.data.get("CIS")));
             if (medicamentOpt.isPresent()) {
                 MedicamentAvis avisSMR = MedicamentAvis.builder()
                         .dateAvis(MedicalDataParser.strToDate(data.data.get("date_avis"), true))
                         .codeDossier(data.data.get("code_dossier"))
                         .motif(data.data.get("motif_evaluation"))
                         .valeur(ValeurAvis.fromString(data.data.get("valeur")))
                         .libelle(data.data.get("libelle"))
                         .typeAvisEnum(TypeAvis.SMR).build();
                 Medicament medicament = medicamentOpt.get();
                 medicament.addAvis(avisSMR);
                 medicamentRepository.save(medicament);
             }
         });
     }

     public void populateInfos(){
         medicalDataParser.initInfos("src/main/resources/data/CIS_InfoImportantes_20221019150103_bdpm.txt");
            List<DataWrapper> list = medicalDataParser.parseInfos();
            list.forEach(data -> {
                Optional<Medicament> medicamentOpt;
                medicamentOpt = medicamentRepository.findByCodeCIS(
                        Integer.parseInt(data.data.get("CIS")));
                if (medicamentOpt.isPresent()) {
                    Medicament medicament = medicamentOpt.get();
                    medicament.addInfo(data.data.get("lien_info"));
                    medicamentRepository.save(medicament);
                }
            });
    }

    public void populateConditions(){
        medicalDataParser.initConditionPrescription("src/main/resources/data/CIS_CPD_bdpm.txt");
        List<DataWrapper> list = medicalDataParser.parseConditionPrescription();
        list.forEach(data -> {
            Optional<ConditionPrescription> conditionPrescriptionOpt = conditionPrescriptionRepository.findByLibelle(data.data.get("libelle"));
            ConditionPrescription conditionPrescription = conditionPrescriptionOpt.orElseGet(
                    () -> ConditionPrescription.builder()
                    .libelle(data.data.get("condition"))
                    .build());
            Optional<Medicament> medicamentOpt = medicamentRepository.findByCodeCIS(
                    Integer.parseInt(data.data.get("CIS")));
            if (medicamentOpt.isPresent()) {
                Medicament medicament = medicamentOpt.get();
                medicament.addConditionsPrescription(conditionPrescription);
                medicamentRepository.save(medicament);
            }
        });
    }




}
