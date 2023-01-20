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
        ArrayList<DataWrapper> result = dp.parseMedicament();
        result.forEach(data -> {
            Map<String, Object> map = data.data;
            System.out.println("###################");
            map.forEach((k,v) -> System.out.println(k+": "+v.toString()));
            assertThat(Integer.parseInt(map.get("CIS").toString())).isPositive();
        });
        assertThat(result.get(0).data.get("CIS").toString()).isEqualTo("61266250");
    }

    @Test
    void parsePresentationFile(){
        dp.initPresentation("src/main/resources/data/CIS_CIP_bdpm.txt");
        List<DataWrapper> result = dp.parsePresentation();
        result.forEach(data -> {
            System.out.println("######################");
            data.data.forEach((k,v) -> {
                System.out.println(k+": "+v.toString());
                assertThat(Integer.parseInt(data.data.get("CIS").toString())).isPositive();
            });
        });
    }
}
