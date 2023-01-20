package fr.miage.gromed;

import fr.miage.gromed.utils.MedicalDataParser;
import fr.miage.gromed.utils.SchemaNameSpace;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ParseTest {

    @Autowired
    MedicalDataParser dp;

    @Test
    void parseMedicamentTest(){
        dp.setMedicamentString("61266250\tA 313 200 000 UI POUR CENT, pommade\tpommade\tcutanée\tAutorisation active\tProcédure nationale\tCommercialisée\t12/03/1998\t\t\t PHARMA DEVELOPPEMENT\tNon\n" +
                "62869109\tA 313 50 000 U.I., capsule molle\tcapsule molle\torale\tAutorisation active\tProcédure nationale\tCommercialisée\t07/07/1997\t\t\t PHARMA DEVELOPPEMENT\tNon\n" +
                "61876780\tABACAVIR ARROW 300 mg, comprimé pelliculé sécable\tcomprimé pelliculé sécable\torale\tAutorisation active\tProcédure décentralisée\tCommercialisée\t22/10/2019\t\t\t ARROW GENERIQUES\tNon\n" +
                "62401060\tABACAVIR MYLAN 300 mg, comprimé pelliculé sécable\tcomprimé pelliculé sécable\torale\tAutorisation active\tProcédure décentralisée\tCommercialisée\t21/02/2018\t\t\t MYLAN SAS\tNon\n" +
                "63797011\tABACAVIR SANDOZ 300 mg, comprimé pelliculé sécable\tcomprimé pelliculé sécable\torale\tAutorisation active\tProcédure décentralisée\tCommercialisée\t30/12/2016\t\t\t SANDOZ\tNon\n" +
                "68257528\tABACAVIR/LAMIVUDINE ACCORD 600 mg/300 mg, comprimé pelliculé\tcomprimé pelliculé\torale\tAutorisation active\tProcédure nationale\tCommercialisée\t16/03/2017\t\t\t ACCORD HEALTHCARE FRANCE\tNon\n" +
                "62828870\tABACAVIR/LAMIVUDINE ARROW 600 mg/300 mg, comprimé pelliculé\tcomprimé pelliculé\torale\tAutorisation active\tProcédure décentralisée\tCommercialisée\t15/12/2017\t\t\t ARROW GENERIQUES\tNon\n" +
                "63431640\tABACAVIR/LAMIVUDINE BIOGARAN 600 mg/300 mg, comprimé pelliculé\tcomprimé pelliculé\torale\tAutorisation active\tProcédure nationale\tCommercialisée\t14/02/2017\t\t\t BIOGARAN\tNon\n" +
                "65196479\tABACAVIR/LAMIVUDINE EG 600 mg/300 mg, comprimé pelliculé\tcomprimé pelliculé\torale\tAutorisation active\tProcédure décentralisée\tCommercialisée\t13/01/2017\t\t\t EG LABO - LABORATOIRES EUROGENERICS\tNon\n" +
                "62170486\tABACAVIR/LAMIVUDINE MYLAN 600 mg/300 mg, comprimé pelliculé\tcomprimé pelliculé\torale\tAutorisation active\tProcédure nationale\tCommercialisée\t03/03/2017\t\t\t MYLAN SAS\tNon\n" +
                "67567004\tABACAVIR/LAMIVUDINE MYLAN PHARMA 600 mg/300 mg, comprimé pelliculé\tcomprimé pelliculé\torale\tAutorisation active\tProcédure décentralisée\tCommercialisée\t08/01/2018\t\t\t MYLAN SAS\tNon");
        ArrayList<Map<String,Object>> result = dp.parseMedicamentTest(SchemaNameSpace.MEDICAMENT);
        result.forEach(map -> {
            System.out.println("###################");
            map.forEach((k,v) -> System.out.println(k+": "+v.toString()));
            assertThat(Integer.parseInt(map.get("CIS").toString())).isPositive();
        });
        assertThat(result.get(0).get("CIS").toString()).isEqualTo("61266250");
        assertThat(result.size()).isEqualTo(11);
    }
}
