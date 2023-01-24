package fr.miage.gromed.service;

import fr.miage.gromed.model.Stock;
import fr.miage.gromed.model.enums.NatureComposant;
import fr.miage.gromed.model.enums.TypeAvis;
import fr.miage.gromed.model.enums.ValeurAvis;
import fr.miage.gromed.model.medicament.*;
import fr.miage.gromed.repositories.MedicamentRepository;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.repositories.StockRepository;
import fr.miage.gromed.utils.DataWrapper;
import fr.miage.gromed.utils.MedicalDataParser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Service
public class PopulateService {

    @Autowired
    private MedicalDataParser medicalDataParser;

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private ConditionPrescriptionRepository conditionPrescriptionRepository;

    private final int batchSize = 100;
    private Map<Integer, String> medicamentLaboMap = new HashMap<>();

    private Map<Integer, Medicament> medicamentCisMap = new HashMap<>();

    private Map<Long, Presentation> presentationMap = new HashMap<>();
    Logger logger = Logger.getLogger(PopulateService.class.getName());
    @Autowired
    private PresentationRepository presentationRepository;


    @Transactional
    public void populateMedicament() {
        medicalDataParser.initMedicament("src/main/resources/data/CIS_bdpm.txt");
        List<DataWrapper> list = medicalDataParser.parseMedicament();
        List<Medicament> medicaments = new ArrayList<>();
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
                            .composantList(new HashSet<ComposantSubtance>())
                            .presentationList(new HashSet<Presentation>())
                            .groupeGeneriqueList(new HashSet<GroupeGenerique>())
                            .medicamentAvisList(new HashSet<MedicamentAvis>())
                            .conditionPrescriptionList(new HashSet<ConditionPrescription>())
                            .numeroAutorisationEuro(data.data.get("numero_autorisation_Europeenne")).build();
            medicamentLaboMap.put(medicament.getCodeCIS(), data.data.get("titulaires"));
            medicaments.add(medicament);
            medicamentCisMap.put(medicament.getCodeCIS(), medicament);
            logger.info("populateMedicament: "+medicament.toString());
            logger.info("###################");
        });
        medicamentRepository.saveAll(medicaments);
    }

//    private void batchInsert(List list, JpaRepository repository){
//        int batchSize = 100;
//        for (int i = 0; i < list.size(); i += batchSize) {
//            List batch = list.subList(i, Math.min(i + batchSize, list.size()));
//            repository.saveAll(batch);
//            repository.flush();
//        }
//    }
        //use medicalDataParser to parse CIS_CIP_bdpm.txt and save the result in the database accordingly to the model using medicamentRepository CIS as foreign key

    @Transactional
    public void populatePresentation() {
         medicalDataParser.initPresentation("src/main/resources/data/CIS_CIP_bdpm.txt");
         List<DataWrapper> list = medicalDataParser.parsePresentation();
         List<Medicament> medicaments = new ArrayList<>();
         list.forEach(data -> {
             Optional<Medicament> medicamentOpt;
             try {
                 int medId = Integer.parseInt(data.data.get("CIS"));
                 medicamentOpt = medicamentRepository.findByCodeCIS(medId);
                 System.out.println(medicamentOpt.toString());
                 if (medicamentOpt.isPresent() && data.data.get("CIP13") != null) {
//                     data.data.put("prix_base", reformatDouble(data.data.get("prix_base")));
//                     data.data.put("honoraire", reformatDouble(data.data.get("honoraire")));
                     Presentation presentation = Presentation.builder()
                             .codeCIP(Long.parseLong(data.data.get("CIP13")))
                             .libelle(data.data.get("libelle"))
//                             .prixDeBase(Double.parseDouble(data.data.get("prix_base")))
//                             .honoraireRemboursement(Double.parseDouble(data.data.get("honoraire")))
//                             .tauxRemboursement(data.data.get("taux_remboursement"))
//                             .isAgrement(Boolean.parseBoolean(data.data.get("agrement_collectivites")))
////                             .dateDeclaration(MedicalDataParser.strToDate(data.data.get("date_declaration_commercialisation"), false))
//                             .etatCommercialisation(data.data.get("etat_commercialisation"))
//                             .statutAdmin(data.data.get("statut_admin"))
                             .build();
                     Medicament medicament = medicamentOpt.get();
                     medicament.addPresentation(presentation);
//                     presentation.setStock(this.generateStock(presentation));
                     medicaments.add(medicament);
                 }
             }
             catch (IllegalArgumentException |  org.springframework.dao.InvalidDataAccessApiUsageException e1){
                 System.out.println("CIS not found "+data.data.get("CIS") + " "+data.data.get("CIP13")+" er: "+ e1.getMessage());
             }

         });
         medicamentRepository.saveAll(medicaments);
//        for (int i = 0; i < medicaments.size(); i += batchSize) {
//            List<Medicament> batch = medicaments.subList(i, Math.min(i + batchSize, medicaments.size()));
//            medicamentRepository.saveAll(batch);
//            medicamentRepository.flush();
//        }
     }

     private double parseDouble (String str) {
         try {
             String result= str;
            if (str.contains(",")) {
                result = str.replace(",", ".");
            }
             return Double.parseDouble(result);
         } catch (NumberFormatException | NullPointerException e) {
             return 0.0;
         }
     }
     private String reformatDouble(String str){
        if (str == null || str.isEmpty() ){
            return "0.0";
        }
         return str.contains(",")?str.replace(",", "."):str;
     }

//     private Stock generateStock (Presentation presentation){
////        Random random = new Random();
////             int stockValue = random.nextInt(100,1000);
////            return Stock.builder().quantiteStockPhysique(stockValue)
////            .quantiteStockLogique(stockValue)
////            .restockAlertFlag(false)
//////                    .presentation(presentation)
////            .build();
//     }
//
//    @Transactional
    public void populatePresCached(){
        medicalDataParser.initPresentation("src/main/resources/data/CIS_CIP_bdpm.txt");
        List<DataWrapper> list = medicalDataParser.parsePresentation();
        Set<Medicament> medicaments = new HashSet<>();
        list.forEach(data -> {
                Medicament medicament = medicamentCisMap.get(Integer.parseInt(data.data.get("CIS")));
                if (medicament != null && data.data.get("CIP13") != null) {
                        data.data.put("agrement_collectivites", sanitizeBool(data.data.get("agrement_collectivites")));
                        Presentation presentation = Presentation.builder()
                                .codeCIP(Long.parseLong(data.data.get("CIP13")))
                                .libelle(data.data.get("libelle"))
                                .prixDeBase(this.parseDouble(data.data.get("prix_base")))
                                .honoraireRemboursement(this.parseDouble(data.data.get("honoraire")))
                                .tauxRemboursement(data.data.get("taux_remboursement"))
                                .isAgrement(Boolean.parseBoolean(data.data.get("agrement_collectivites")))
                                .dateDeclaration(MedicalDataParser.strToDate(data.data.get("date_declaration_commercialisation"), false))
                                .etatCommercialisation(data.data.get("etat_commercialisation"))
                                .statutAdmin(data.data.get("statut_admin"))
                                .build();
                        medicament.addPresentation(presentation);
                        medicaments.add(medicament);
                }

        });
        medicamentRepository.saveAll(medicaments);
    }
    @Autowired
    private StockRepository stockRepository;

    @Transactional
    public void initStock(){
        List<Presentation> presentations = presentationRepository.findAll();
        List<Stock> stocks = new ArrayList<>();
        presentations.forEach(presentation -> {
            Stock stock = new Stock();
            Random random = new Random();
            int stockValue = random.nextInt(101,1000);
            stock.setQuantiteStockLogique(stockValue);
            stock.setQuantiteStockPhysique(stockValue);
            presentation.setStock(stock);
            stocks.add(stock);
        });
        stockRepository.saveAll(stocks);
        presentationRepository.saveAll(presentations);
    }

    private String sanitizeBool(String agrement_collectivites) {
        if (agrement_collectivites == null || agrement_collectivites.isEmpty()){
            return "false";
        }
        return agrement_collectivites;
    }


    @Transactional
     public void populateComposantCached(){
         medicalDataParser.initComposants("src/main/resources/data/CIS_COMPO_bdpm.txt");
         List<DataWrapper> list = medicalDataParser.parseComposant();
         List<Medicament> medicaments = new ArrayList<>();
         list.forEach(data -> {
             if (!medicamentCisMap.isEmpty()) {
                 Medicament medicament = medicamentCisMap.get(Integer.parseInt(data.data.get("CIS")));
                 if (medicament != null) {
                     System.out.println("medocCached: "+medicament.toString());
                     ComposantSubtance composantSubtance = ComposantSubtance.builder()
                             .codeSubstance(Integer.parseInt(data.data.get("code_substance")))
                             .denomination(data.data.get("denomination"))
                             .designationElementPharmaceutique(data.data.get("designation_element_pharmaceutique"))
                             .natureComposant(NatureComposant.fromString(data.data.get("nature_composant")))
                             .dosage(data.data.get("dosage_substance"))
                             .referenceDosage(data.data.get("reference_dosage"))
                             .build();
                     medicament.addComposant(composantSubtance);
                     medicaments.add(medicament);
                 }
             }
         });
             medicamentRepository.saveAll(medicaments);
     }

//     @Transactional
     public void populateComposant() {
         medicalDataParser.initComposants("src/main/resources/data/CIS_COMPO_bdpm.txt");
         List<DataWrapper> list = medicalDataParser.parseComposant();
         List<Medicament> medicaments = new ArrayList<>();
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
                 medicaments.add(medicament);
             }
         });
         for (int i = 0; i < medicaments.size(); i += batchSize) {
             List<Medicament> batch = medicaments.subList(i, Math.min(i + batchSize, medicaments.size()));
             medicamentRepository.saveAll(batch);
             medicamentRepository.flush();
         }
     }

     @Transactional
     public void populateAvis(){
        medicalDataParser.initAvisASMR("src/main/resources/data/CIS_HAS_ASMR_bdpm.txt");
        medicalDataParser.initAvisSMR("src/main/resources/data/CIS_HAS_SMR_bdpm.txt");

        List<Medicament> medicaments = new ArrayList<>();

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
//                 medicamentRepository.save(medicament);
                 medicaments.add(medicament);
             }
         });
         for (int i = 0; i < medicaments.size(); i += batchSize) {
             List<Medicament> batch = medicaments.subList(i, Math.min(i + batchSize, medicaments.size()));
             medicamentRepository.saveAll(batch);
             medicamentRepository.flush();
         }
     }

     public void populateInfos(){
        List<Medicament> medicaments = new ArrayList<>();
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
                    medicaments.add(medicament);
                }
            });
         for (int i = 0; i < medicaments.size(); i += batchSize) {
             List<Medicament> batch = medicaments.subList(i, Math.min(i + batchSize, medicaments.size()));
             medicamentRepository.saveAll(batch);
             medicamentRepository.flush();
         }
    }

    public void populateConditions(){
        List<Medicament> medicaments = new ArrayList<>();

        medicalDataParser.initConditionPrescription("src/main/resources/data/CIS_CPD_bdpm.txt");
        List<DataWrapper> list = medicalDataParser.parseConditionPrescription();
        list.forEach(data -> {
            Optional<Medicament> medicamentOpt = medicamentRepository.findByCodeCIS(
                    Integer.parseInt(data.data.get("CIS")));
            if (medicamentOpt.isPresent()) {
                Optional<ConditionPrescription> conditionPrescriptionOpt = conditionPrescriptionRepository.findByLibelle(data.data.get("libelle"));
                ConditionPrescription conditionPrescription = conditionPrescriptionOpt.orElseGet(
                        () -> ConditionPrescription.builder()
                                .libelle(data.data.get("condition"))
                                .build());
                Medicament medicament = medicamentOpt.get();
                medicament.addConditionsPrescription(conditionPrescription);
                medicamentRepository.save(medicament);
                medicaments.add(medicament);
            }
        });
        for (int i = 0; i < medicaments.size(); i += batchSize) {
            List<Medicament> batch = medicaments.subList(i, Math.min(i + batchSize, medicaments.size()));
            medicamentRepository.saveAll(batch);
            medicamentRepository.flush();
        }
    }




}
