package fr.miage.gromed;

import fr.miage.gromed.utils.DataWrapper;
import fr.miage.gromed.utils.MedicalDataParser;
import fr.miage.gromed.utils.SchemaNameSpace;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ParseTest {

    static Logger log = java.util.logging.Logger.getLogger(ParseTest.class.getName());

    @Autowired
    MedicalDataParser dp;

    @Test
    void parseMainFile(){
        dp.initMedicament("src/main/resources/data/CIS_bdpm.txt");
        List<DataWrapper> result = dp.parseMedicament();
        result.forEach(data -> {
            Map<String, String> map = data.data;
            System.out.println("###################");
            map.forEach((k,v) -> System.out.println(k+": "+v));
            assertThat(Integer.parseInt(map.get("CIS"))).isPositive();
        });
        assertThat(result.get(0).data.get("CIS")).isEqualTo("61266250");
        log.info(result.get(0).data.get("CIS"));
    }

    @Test
    void parsePresentationFile(){
        dp.initPresentation("src/main/resources/data/CIS_CIP_bdpm.txt");
        List<DataWrapper> result = dp.parsePresentation();
        iterateResult(result, "######################");
    }

    @Test
    void parseComposantFile(){
        dp.initComposants("src/main/resources/data/CIS_COMPO_bdpm.txt");
        List<DataWrapper> result = dp.parseComposants();
        iterateResult(result, "######################");
    }

    @Test
    void parseAvis() {
        dp.initAvisASMR("src/main/resources/data/CIS_HAS_ASMR_bdpm.txt");
        dp.initAvisSMR("src/main/resources/data/CIS_HAS_SMR_bdpm.txt");

        List<DataWrapper> resultAsmr = dp.parseASMR();
        List<DataWrapper> resultSmr = dp.parseSMR();

        iterateResult(resultAsmr, "ASMR######################");

        iterateResult(resultSmr, "SMR######################");
    }

    @Test
    void parseInfos(){
        dp.initInfos("src/main/resources/data/CIS_InfoImportantes_20221019150103_bdpm.txt");
        List<DataWrapper> result = dp.parseInfos();
        iterateResult(result, "######################");
    }

    @Test
    void parseCondition(){
        dp.initConditionPrescription("src/main/resources/data/CIS_CPD_bdpm.txt");
        List<DataWrapper> result = dp.parseConditionPrescription();
        iterateResult(result, "######################");
    }

    @Test
    void parseGenerique(){
        dp.initGenerique("src/main/resources/data/CIS_GENER_bdpm.txt");
        List<DataWrapper> result = dp.parseGenerique();
        iterateResult(result, "######################");
    }


    private static void iterateResult(List<DataWrapper> result, String x) {
        result.forEach(data -> {
            System.out.println(x);
            data.data.forEach((k, v) -> {
                log.info(k + ": " + v);
                assertThat(Integer.parseInt(data.data.get("CIS"))).isPositive();
            });
        });
    }
}
