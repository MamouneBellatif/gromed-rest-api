package fr.miage.gromed;

import fr.miage.gromed.controller.PresentationController;
import fr.miage.gromed.service.MedicamentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GroMedApplicationTests {

	@Autowired
	PresentationController presentationController;

	@Autowired
	MedicamentService medicamentService;

	private static final int NB_MEDICAMENT = 0;
	@Test
	void contextLoads() {
		//assertThat(true).isTrue();
		assertThat(presentationController).isNotNull();
	}

	@Test
	void testMedicamentCount(){
		assertThat(medicamentService.medicamentCount()).isEqualTo(NB_MEDICAMENT);
	}
}
