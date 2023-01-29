package fr.miage.gromed.service.auth;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;


//@Service
public class FBservice {

        @PostConstruct
        public void initialize() {
            try {
                // FileInputStream serviceAccount = new FileInputStream(
                //         "src/main/java/com/example/service/integrateur2022g5-firebase-adminsdk-5ncvw-49a213bd7a.json");

                FirebaseOptions options = new FirebaseOptions.Builder()
                        // .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .build();

                FirebaseApp.initializeApp(options);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

}
