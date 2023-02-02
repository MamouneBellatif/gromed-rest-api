package fr.miage.gromed;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;


@SpringBootApplication
@EnableScheduling
@EnableAsync
public class GroMedApplication {
	static Logger log = Logger.getLogger(GroMedApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(GroMedApplication.class, args);
		initFireBase();
	}

	private static void initFireBase() {
		try {
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(new FileInputStream("src/main/resources/gromed-3c731-firebase-adminsdk-sxq2j-4582cb1de5.json")))
                        .setDatabaseUrl("https://gromed-3c731-default-rtdb.europe-west1.firebasedatabase.app")
					.build();
			log.info("Firebase initialized");
			if(FirebaseApp.getApps().isEmpty()) { //<--- check with this line
				FirebaseApp.initializeApp(options,"gromed-3c731");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
