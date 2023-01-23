package fr.miage.gromed.utils;

import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

@Component
@Setter
public class MedicalDataParser {

    private static final String NEWLINE = "\n";
    private static final String SEPARATOR = "\t";
    Logger log = Logger.getLogger(MedicalDataParser.class.getName());
    Map<SchemaNameSpace, String[]> dbSchema = new HashMap<>();
    Map<SchemaNameSpace, String> fileContent = new HashMap<>();

    Map<SchemaNameSpace, Boolean> isInit = new HashMap<>();

    public void initGenerique(String path){
        dbSchema.put(SchemaNameSpace.GROUPE_GENERIQUE,new String[]{
                "id_generique",
                "libelle_generique",
                "CIS",
                "type_generique"
        });
        initFileContent(SchemaNameSpace.GROUPE_GENERIQUE, path);
        isInit.put(SchemaNameSpace.GROUPE_GENERIQUE, true);
    }

    public List<DataWrapper> parseGenerique(){
        return this.parse(SchemaNameSpace.GROUPE_GENERIQUE);
    }



    public void initComposants(String composantPath){
        dbSchema.put(SchemaNameSpace.COMPOSANT, new String[] {
                "CIS",
                "designation_element_pharmaceutique",
                "code_substance",
                "denomination",
                "dosage_substance",
                "reference_dosage",
                "nature_composant",
                "numero_liaison_sa_ft"
        });
        initFileContent(SchemaNameSpace.COMPOSANT, composantPath);
        isInit.put(SchemaNameSpace.COMPOSANT, true);
    }

    public void initInfos(String path){
        dbSchema.put(SchemaNameSpace.INFOS,new String[]{
            "CIS",
            "date_debut",
            "date_fin",
            "lien_info"
        });
        initFileContent(SchemaNameSpace.INFOS, path);
        isInit.put(SchemaNameSpace.INFOS, true);
    }

    public List<DataWrapper> parseInfos(){
        return parse(SchemaNameSpace.INFOS);
    }

    public void initPresentation(String presentationPath){
        SchemaNameSpace ns = SchemaNameSpace.PRESENTATION;
        dbSchema.put(ns, new String[] {
                "CIS",
                "CIP7",
                "libelle",
                "statut_admin",
                "etat_commercialisation",
                "date_declaration_commercialisation",
                "CIP13",
                "agrement_collectivites",
                "taux_remboursement",
                "prix_base",
                "prix_avec_remboursement",
                "honoraire",
                "indications_remboursement"
        });
        initFileContent(ns, presentationPath);
        isInit.put(ns,true);
    }
    public void initAvisASMR(String avisPathASMR){
        dbSchema.put(SchemaNameSpace.AVIS_ASMR, new String[]{
                "CIS",
                "code_dossier",
                "motif_evaluation",
                "date_avis",
                "valeur",
                "libelle",
        });
        initFileContent(SchemaNameSpace.AVIS_ASMR, avisPathASMR);
        isInit.put(SchemaNameSpace.AVIS_ASMR,true);
    }

    public List<DataWrapper> parseASMR(){
        return this.parse(SchemaNameSpace.AVIS_ASMR);
    }

    public List<DataWrapper> parseSMR(){
        return this.parse(SchemaNameSpace.AVIS_SMR);
    }


    public void initAvisSMR(String avisPathSMR){
        dbSchema.put(SchemaNameSpace.AVIS_SMR, new String[]{
                "CIS",
                "code_dossier",
                "motif_evaluation",
                "date_avis",
                "valeur",
                "libelle",
        });
        initFileContent(SchemaNameSpace.AVIS_SMR, avisPathSMR);
        isInit.put(SchemaNameSpace.AVIS_SMR,true);
    }

    public void initConditionPrescription(String conditionPath){
        dbSchema.put(SchemaNameSpace.CONDITION_PRESCRIPTION, new String[]{
                "CIS",
                "condition"
        });
        initFileContent(SchemaNameSpace.CONDITION_PRESCRIPTION, conditionPath);
        isInit.put(SchemaNameSpace.CONDITION_PRESCRIPTION,true);
    }

    public List<DataWrapper> parseConditionPrescription(){
        return this.parse(SchemaNameSpace.CONDITION_PRESCRIPTION);
    }



    public void initMedicament(String medicamentPath) {
        dbSchema.put(SchemaNameSpace.MEDICAMENT, new String[]{
                "CIS",
                "denomination",
                "forme_pharmaceutique",
                "voies_administration",
                "statut_admin_AMM",
                "type_procedure_AMM",
                "etat_commercialisation",
                "date_AMM",
                "statut_BDM",
                "numero_autorisation_europeenne",
                "titulaires",
                "surveillance_renforcee"
        });
        initFileContent(SchemaNameSpace.MEDICAMENT, medicamentPath);
        isInit.put(SchemaNameSpace.MEDICAMENT,true);
    }

    public List<DataWrapper> parsePresentation(){
        return this.parse(SchemaNameSpace.PRESENTATION);
    }

    public List<DataWrapper> parseComposants(){
        return this.parse(SchemaNameSpace.COMPOSANT);
    }
    private void initFileContent(SchemaNameSpace fileType, String filePath){
        try {
            var file = new FileInputStream(new File(filePath));
            fileContent.put(fileType,FileHelper.readFromInputStream(file));
        }catch (Exception e) {
            log.warning("Fichier de présentation non trouvé");
        }
    }
    private DataWrapper parseLine(SchemaNameSpace namespace, String line) {
        String[] values = line.split(SEPARATOR);
        String[] schema = dbSchema.get(namespace);
        Map<String, String> data = new LinkedHashMap<>();
        for (int i = 0; i < schema.length && i < values.length; i++) {
            String key = schema[i];
            String value = values[i];
            data.put(key,value);
        }
        return new DataWrapper(data);
    }

    public static Date strToDate(String dateString, boolean isAvis) {
         SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
         if (isAvis) {
             dateFormat = new SimpleDateFormat("yyyyMMdd");
         }
        try {
            if(dateString!=null) {
                return dateFormat.parse(dateString);
            }
            return null;
        } catch (ParseException e) {
            return null;
        }
    }



    public List<DataWrapper> parseMedicament(){
        return this.parse(SchemaNameSpace.MEDICAMENT);
    }

    public List<DataWrapper> parse(SchemaNameSpace namespace){
        if (!(isInit.containsKey(namespace) && isInit.get(namespace))){
            return null;
        }
        String[] lines = this.fileContent.get(namespace).split(NEWLINE);
        ArrayList<DataWrapper> result = new ArrayList<>();
        Arrays.stream(lines).forEach(line -> result.add(parseLine(namespace, this.trimEnd(line))));
        return result;
    }

    private Object booleanParser(String value) {
        if (value.equalsIgnoreCase("non")) return false;
        if (value.equalsIgnoreCase("oui")) return true;
        return value;
    }

    private Object integerParser(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return value;
        }
    }
    public Object doubleParser(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException  | NullPointerException e) {
            return value;
        }
    }

    private String trimEnd(String value) {
        int len = value.length();
        int st = 0;
        while ((st < len) && value.charAt(len - 1) == ' ') {
            len--;
        }
        return value.substring(0, len);
    }

    public List<DataWrapper> parseComposant() {
        return this.parse(SchemaNameSpace.COMPOSANT);
    }



    private static class FileHelper {
        private static String readFromInputStream(InputStream inputStream)
                throws IOException {
            StringBuilder resultStringBuilder = new StringBuilder();
            try (BufferedReader br
                         = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("windows-1252")))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append(NEWLINE);
                }
            }
            return resultStringBuilder.toString();
        }
    }
}

