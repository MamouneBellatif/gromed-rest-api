package fr.miage.gromed.utils;

import fr.miage.gromed.restockfaker.RestockFaker;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.*;
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

    private String medicamentString;

    private void initFileContent(SchemaNameSpace fileType, String filePath){
        try {
            var file = new FileInputStream(new File(filePath));
            fileContent.put(fileType,FileHelper.readFromInputStream(file));
        }catch (Exception e) {
            log.warning("Fichier de présentation non trouvé");
        }
    }

//    public void
    public MedicalDataParser() {

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
    }

    public void initPresentation(String presentationPath){
        dbSchema.put(SchemaNameSpace.PRESENTATION, new String[] {
                "CIS",
                "CIP7",
                "libelle",
                "statut_admin",
                "etat_commercialisation",
                "date_declaration_commercialisation",
                "CIP13",
                "agrement_collectivites",
                "taux_remboursement",
                "prix_sans_honoraires",
                "prix_avec_honoraires",
                "honoraires",
                "indications_remboursement"
        });
        initFileContent(SchemaNameSpace.PRESENTATION, presentationPath);
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
    }

    public Map<String, Object> parseLine(SchemaNameSpace namespace, String line) {
        String[] values = line.split(SEPARATOR);
        String[] schema = dbSchema.get(namespace);
        Map<String, Object> data = new LinkedHashMap<>();
        for (int i = 0; i < schema.length; i++) {
            String key = schema[i];
            String value = values[i];
            Object parsedIntegerValue = integerParser(value);
            data.put(key, booleanParser(value));
        }
        return data;
    }

    public ArrayList<Map<String, Object>> parse(SchemaNameSpace namespace){
        String[] lines = this.fileContent.get(namespace).split(NEWLINE);
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        Arrays.stream(lines).forEach(line ->{
            result.add(parseLine(namespace, this.trimEnd(line)));
        });
        return result;
    }
    public ArrayList<Map<String, Object>> parseMedicamentTest(SchemaNameSpace namespace){
        String[] dataLines = medicamentString.split(NEWLINE);
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        Arrays.stream(dataLines).forEach(line ->{
            result.add(parseLine(namespace, this.trimEnd(line)));
        });
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


    private static class FileHelper {
        private static String readFromInputStream(InputStream inputStream)
                throws IOException {
            StringBuilder resultStringBuilder = new StringBuilder();
            try (BufferedReader br
                         = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append(NEWLINE);
                }
            }
            return resultStringBuilder.toString();
        }
    }
}

