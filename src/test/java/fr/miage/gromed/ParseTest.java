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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ParseTest {

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
        System.out.println(result.get(0).data.get("CIS"));
    }

    @Test
    void parsePresentationFile(){
        dp.initPresentation("src/main/resources/data/CIS_CIP_bdpm.txt");
        List<DataWrapper> result = dp.parsePresentation();
        result.forEach(data -> {
            System.out.println("######################");
            data.data.forEach((k,v) -> {
                System.out.println(k+": "+v);
                assertThat(Integer.parseInt(data.data.get("CIS"))).isPositive();
            });
        });
    }

    @Test
    void parseComposantFile(){
        dp.initComposants("src/main/resources/data/CIS_COMPO_bdpm.txt");
        List<DataWrapper> result = dp.parseComposants();
        result.forEach(data -> {
            System.out.println("######################");
            data.data.forEach((k,v) -> {
                System.out.println(k+": "+v);
                assertThat(Integer.parseInt(data.data.get("CIS"))).isPositive();
            });
        });
    }

    @Test
    void parseAvis() {
        dp.initAvisASMR("src/main/resources/data/CIS_HAS_ASMR_bdpm.txt");
        dp.initAvisSMR("src/main/resources/data/CIS_HAS_SMR_bdpm.txt");

        List<DataWrapper> resultAsmr = dp.parseASMR();
        List<DataWrapper> resultSmr = dp.parseSMR();

        resultAsmr.forEach(data -> {
            System.out.println("ASMR######################");
            data.data.forEach((k,v) -> {
                System.out.println(k+": "+v);
                assertThat(Integer.parseInt(data.data.get("CIS"))).isPositive();
            });
        });

        resultSmr.forEach(data -> {
            System.out.println("SMR######################");
            data.data.forEach((k,v) -> {
                System.out.println(k+": "+v);
                assertThat(Integer.parseInt(data.data.get("CIS"))).isPositive();
            });
        });
    }
}
