package fr.miage.gromed.service.auth;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import com.google.firebase.auth.UserRecord;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter implements Filter {

Logger logger = Logger.getLogger(AuthFilter.class.getName());
//@Autowired
//private FBinit fbinit;


//    try{
//                FileInputStream serviceAccount =
//                        new FileInputStream("src/main/resources/gromed-3c731-firebase-adminsdk-sxq2j-4582cb1de5.json");
//
//                FirebaseOptions options = new FirebaseOptions.Builder()
//                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
////                        .setDatabaseUrl("https://gromed-3c731-default-rtdb.europe-west1.firebasedatabase.app")
//                        .build();
//
//                FirebaseApp.initializeApp(options);
//                logger.info("Firebase initialized" + FirebaseApp.getInstance().toString());
//    }catch (Exception e) {
//        e.printStackTrace();
//    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, jakarta.servlet.FilterChain chain) throws IOException, jakarta.servlet.ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        String idToken = httpRequest.getHeader("Authorization");
        String idToken = httpRequest.getHeader("uid");
        logger.info("idToken : " + idToken);
//        if (idToken == null || !idToken.startsWith("Bearer ")) {
        if (idToken == null) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }
        logger.info("idToken bear : " + idToken);

//        idToken = idToken.substring("Bearer ".length());
        logger.info("sub: " + idToken);

        try {
            UserRecord userRecord = FirebaseAuth.getInstance(FirebaseApp.getInstance("gromed-3c731")).getUser(idToken);

//            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
//            logger.info("sub2: " + firebaseToken.getUid());
//            httpRequest.setAttribute("firebaseToken", firebaseToken);
            chain.doFilter(request, response);
        } catch (FirebaseAuthException e) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Failed to verify Firebase ID token: " + e.getMessage());
        }
    }

    @Override
    public void destroy() {
        // Clean up resources.
    }

}
