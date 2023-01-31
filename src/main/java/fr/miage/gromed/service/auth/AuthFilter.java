package fr.miage.gromed.service.auth;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import java.io.FileInputStream;
import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
public class AuthFilter implements Filter {


public AuthFilter()   {
    try{
    FileInputStream serviceAccount =  new FileInputStream("src/main/resources/gromed-3c731-firebase-adminsdk-sxq2j-0ca94f20d9.json");

    FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();
    FirebaseApp.initializeApp(options);
    }catch (Exception e) {
        e.printStackTrace();
    }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, jakarta.servlet.FilterChain chain) throws IOException, jakarta.servlet.ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String idToken = httpRequest.getHeader("Authorization");
        if (idToken == null || !idToken.startsWith("Bearer ")) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        idToken = idToken.substring("Bearer ".length());

        try {
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            httpRequest.setAttribute("firebaseToken", firebaseToken);
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
