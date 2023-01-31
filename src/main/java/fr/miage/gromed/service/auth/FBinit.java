//package fr.miage.gromed.service.auth;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import jakarta.annotation.PostConstruct;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import java.io.FileInputStream;
//import java.util.logging.Logger;
//
//@Service
//public class FBinit {
//    Logger log = Logger.getLogger(FBinit.class.getName());
//    @PostConstruct
//    public void initFirebase(){
//        try{
//            GoogleCredentials googleCredentials = GoogleCredentials
//                    .fromStream(new ClassPathResource("src/main/resources/gromed-3c731-firebase-adminsdk-sxq2j-4582cb1de5.json").getInputStream());
//            FirebaseOptions firebaseOptions = FirebaseOptions
//                    .builder()
//                    .setCredentials(googleCredentials)
//                    .build();
//            FirebaseApp app = null;
//            if(FirebaseApp.getApps().isEmpty()) {
//                app = FirebaseApp.initializeApp(firebaseOptions, "appName");
//            }else {
//                app = FirebaseApp.initializeApp(firebaseOptions);
//            }
//
//            return FirebaseMessaging.getInstance(app);
//            FileInputStream serviceAccount =
//                    new FileInputStream("src/main/resources/gromed-3c731-firebase-adminsdk-sxq2j-4582cb1de5.json");
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
////                        .setDatabaseUrl("https://gromed-3c731-default-rtdb.europe-west1.firebasedatabase.app")
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//            log.info("Firebase initialized" + FirebaseApp.getInstance().toString());
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
