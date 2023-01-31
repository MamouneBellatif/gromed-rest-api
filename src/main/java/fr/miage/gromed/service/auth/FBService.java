package fr.miage.gromed.service.auth;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;


//@Service
public class FBService {

//        @PostConstruct
        public void initialize() {
            try {
                FileInputStream serviceAccount =
                        new FileInputStream("src/main/resources/gromed-3c731-firebase-adminsdk-sxq2j-0ca94f20d9.json");

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://gromed-3c731-default-rtdb.europe-west1.firebasedatabase.app")
                        .build();

                FirebaseApp.initializeApp(options);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void checkToken(String token, String uid) {

        }

}
