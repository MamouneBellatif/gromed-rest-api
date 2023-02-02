package fr.miage.gromed.service.metier;

import fr.miage.gromed.model.Stock;
import fr.miage.gromed.model.enums.NatureComposant;
import fr.miage.gromed.model.enums.TypeAvis;
import fr.miage.gromed.model.enums.TypeGenerique;
import fr.miage.gromed.model.enums.ValeurAvis;
import fr.miage.gromed.model.medicament.*;
import fr.miage.gromed.repositories.ConditionPrescriptionRepository;
import fr.miage.gromed.repositories.MedicamentRepository;
import fr.miage.gromed.repositories.PresentationRepository;
import fr.miage.gromed.repositories.StockRepository;
import fr.miage.gromed.utils.DataWrapper;
import fr.miage.gromed.utils.MedicalDataParser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO: Parse et populate les etablissements, et es avis et les géneriques
//todo: critères de recherche
@Service
public class PopulateService {

    @Autowired
    private MedicalDataParser medicalDataParser;

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private ConditionPrescriptionRepository conditionPrescriptionRepository;

    @Autowired
    private StockRepository stockRepository;

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
                    .informationImportantesHtmlAnchor(new HashSet<String>())
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

    public void parseGenerique(){
        medicalDataParser.initGenerique("src/main/resources/data/CIS_GENER_bdpm.txt");
        List<DataWrapper> list = medicalDataParser.parseGenerique();
        List<Medicament> medicaments = medicamentRepository.findAll();
        Map<Integer, Medicament> medicamentMap = medicaments.stream().collect(Collectors.toMap(Medicament::getCodeCIS, medicament -> medicament));
        list.forEach(data -> {
            System.out.println(data.data.get("CIS"));
            try{
                TypeGenerique typeGenerique = TypeGenerique.fromPosition(Integer.parseInt(data.data.get("type_generique")));
                Medicament medicament = medicamentMap.get(Integer.parseInt(data.data.get("CIS")));
                GroupeGenerique groupeGenerique = GroupeGenerique.builder()
                                .typeGeneriqueEnum(typeGenerique)
                                .codeGenerique(data.data.get("id_generique"))
                                .denominationGenerique(data.data.get("denomination_generique"))
                                .build();
                medicament.getGroupeGeneriqueList().add(groupeGenerique);
            } catch (Exception e){
                logger.info("parseGenerique: "+e.getMessage());
            }
        });
        medicamentRepository.saveAll(medicaments);
    }

    public void parseCisLibelle(){
        medicalDataParser.initMedicament("src/main/resources/data/CIS_bdpm.txt");

        List<DataWrapper> list = medicalDataParser.parseMedicament();
        try {
        FileWriter writer = new FileWriter("output.csv");

            list.forEach(data -> {
                System.out.println(data.data.get("CIS"));
                //write in a file the cis and the denomination in a csv format

                    String key = data.data.get("CIS");
                    String value = data.data.get("denomination");
                    String[] words = value.split(" ");
//                    String firstWord = words[0];
//                    try {
//                        writer.write(key + "," + value + "\n");
                    String firstPart = getFirstPart(value);
                    try {
                    writer.write(key + "," + firstPart + "\n");
                    } catch (Exception e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }

            }); writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    private static String getFirstPart(String value) {
        Pattern pattern = Pattern.compile("^([^,]+),(\\d+\\.\\d+|\\d+)");
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return value.split(",")[0];
        }
    }


        //use medicalDataParser to parse CIS_CIP_bdpm.txt and save the result in the database accordingly to the model using medicamentRepository CIS as foreign key


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


    @Transactional
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
//                                .isAgrement(Boolean.parseBoolean(data.data.get("agrement_collectivites")))
                                .isAgrement(MedicalDataParser.booleanParser(data.data.get("agrement_collectivites")))
                                .dateDeclaration(MedicalDataParser.strToDate(data.data.get("date_declaration_commercialisation"), false))
                                .etatCommercialisation(data.data.get("etat_commercialisation"))
                                .statutAdmin(data.data.get("statut_admin"))
                                .medicament(medicament)
                                .build();
                        medicament.addPresentation(presentation);
                        medicaments.add(medicament);
                }

        });
        medicamentRepository.saveAll(medicaments);
    }


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
            return "non";
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

     @Transactional
     public void populateComposant() {

         medicalDataParser.initComposants("src/main/resources/data/CIS_COMPO_bdpm.txt");
         List<DataWrapper> list = medicalDataParser.parseComposant();
         Map<Integer,Medicament> fetchedMedicaments = medicamentRepository.findAll().stream().collect(Collectors.toMap(Medicament::getCodeCIS, medicament -> medicament));
         List<Medicament> medicaments = new ArrayList<>();
         list.forEach(data -> {
             Medicament medicament = fetchedMedicaments.get(Integer.parseInt(data.data.get("CIS")));
                 try {
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
                 } catch (Exception e) {
                     logger.warning("Error parsing code substance: " + data.data.get("code_substance"));
                 }
         });
             medicamentRepository.saveAll(medicaments);
     }

     @Transactional
     public void populateAvis(){
        medicalDataParser.initAvisASMR("src/main/resources/data/CIS_HAS_ASMR_bdpm.txt");
        medicalDataParser.initAvisSMR("src/main/resources/data/CIS_HAS_SMR_bdpm.txt");

         List<Medicament> medicaments = medicamentRepository.findAll();
         List<MedicamentAvis> avisList = new ArrayList<>();
         Map<Integer, Medicament> medicamentCisMap = medicaments.stream().collect(Collectors.toMap(Medicament::getCodeCIS, medicament -> medicament));

        List<DataWrapper> resultAsmr = medicalDataParser.parseASMR();
        resultAsmr.forEach(data -> data.data.put("type_avis", "ASMR"));
        List<DataWrapper> resultSmr = medicalDataParser.parseSMR();
        resultSmr.forEach(data -> data.data.put("type_avis", "SMR"));
//        resultAsmr.addAll(resultSmr);
        Stream.concat(resultSmr.stream(), resultAsmr.stream()).forEach(data -> {
            Medicament medicament = medicamentCisMap.get(Integer.parseInt(data.data.get("CIS")));
            if (medicament != null) {
            MedicamentAvis avisSMR = MedicamentAvis.builder()
                         .dateAvis(MedicalDataParser.strToDate(data.data.get("date_avis"), true))
                         .codeDossier(data.data.get("code_dossier"))
                         .motif(data.data.get("motif_evaluation"))
                         .valeur(ValeurAvis.fromString(data.data.get("valeur")))
                         .libelle(data.data.get("libelle"))
                         .typeAvisEnum(TypeAvis.fromString(data.data.get("type_avis"))).build();
                 medicament.addAvis(avisSMR);
                 medicaments.add(medicament);
                 avisList.add(avisSMR);
            }
         });

             medicamentRepository.saveAll(medicaments);

     }

     @Transactional
     public void populateInfos(){
         List<Medicament> medicaments = medicamentRepository.findAll();
         Map<Integer, Medicament> medicamentCisMap = medicaments.stream().collect(Collectors.toMap(Medicament::getCodeCIS, medicament -> medicament));         medicalDataParser.initInfos("src/main/resources/data/CIS_InfoImportantes_20221019150103_bdpm.txt");
            List<DataWrapper> list = medicalDataParser.parseInfos();
            list.forEach(data -> {
                Medicament medicament = medicamentCisMap.get(Integer.parseInt(data.data.get("CIS")));
                    medicament.addInfo(data.data.get("lien_info"));
                    medicaments.add(medicament);
                }
            );
         medicamentRepository.saveAll(medicaments);

    }

    @Transactional
    public void populateUrls(){
        List<Medicament> medicaments = medicamentRepository.findAll();
        Map<Integer, Medicament> medicamentCisMap = medicaments.stream().collect(Collectors.toMap(Medicament::getCodeCIS, medicament -> medicament));
        medicalDataParser.initUrls("src/main/resources/data/urlsImages.csv");
        List<DataWrapper> list = medicalDataParser.parseUrls();
        list.forEach(data -> {
            Medicament medicament = medicamentCisMap.get(Integer.parseInt(data.data.get("CIS")));
            if (medicament != null) {
                medicament.setUrlImage(data.data.get("url"));
            }
        });
        medicamentRepository.saveAll(medicaments);
    }

    @Transactional
    public void populateConditions(){
        List<Medicament> medicaments = new ArrayList<>();

        medicalDataParser.initConditionPrescription("src/main/resources/data/CIS_CPD_bdpm.txt");
        List<DataWrapper> list = medicalDataParser.parseConditionPrescription();
        Map<String,String> cisConditionMap = new HashMap<>();
        list.forEach(data -> {
            cisConditionMap.put(data.data.get("CIS"),data.data.get("condition_prescription"));
        });
//        Map<String,String> cisConditionMap = list.stream().collect(Collectors.toMap(data -> data.data.get("CIS"), data -> data.data.get("condition")));
        Map<String, List<String>> newMap = cisConditionMap.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));

        Stream<String> libelleList = list.stream().map(data -> data.data.get("condition"));
        Stream<String> distinctLibelleList = libelleList.distinct();
        Set<ConditionPrescription> conditionPrescriptions = new HashSet<>();
        distinctLibelleList.forEach(libelle -> {
            ConditionPrescription conditionPrescription = ConditionPrescription.builder()
                    .libelle(libelle)
                    .build();
            conditionPrescriptions.add(conditionPrescription);
            List<Integer> cis = newMap.get(libelle).stream().map(Integer::parseInt).collect(Collectors.toList());
            List<Medicament> medicamentList = medicamentRepository.findAllByCodeCISIn(cis);
            medicamentList.forEach(medicament -> {
                medicament.addConditionPrescription(conditionPrescription);
                medicaments.add(medicament);
            });
            //            cis.forEach(cisStr -> {
//                Optional<Medicament> medicamentOpt;
//                medicamentOpt = medicamentRepository.findByCodeCIS(
//                        Integer.parseInt(cisStr));
//                if (medicamentOpt.isPresent()) {
//                    Medicament medicament = medicamentOpt.get();
//                    medicament.addCondition(conditionPrescription);
//                    medicaments.add(medicament);
//                }
//            });
        });

        conditionPrescriptionRepository.saveAll(conditionPrescriptions);

        medicamentRepository.saveAll(medicaments);
    }




}
