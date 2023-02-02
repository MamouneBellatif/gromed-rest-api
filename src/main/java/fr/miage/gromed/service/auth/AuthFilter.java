package fr.miage.gromed.service.auth;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import com.google.firebase.auth.UserRecord;
import fr.miage.gromed.dto.PanierItemDto;
import fr.miage.gromed.service.UtilisateurService;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AuthFilter implements Filter {

Logger logger = Logger.getLogger(AuthFilter.class.getName());

    private UtilisateurService utilisateurService;

    @Autowired
    public AuthFilter(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, jakarta.servlet.FilterChain chain) throws IOException, jakarta.servlet.ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        logger.info("AuthFilter");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
//        httpResponse.setHeader("Access-Control-Max-Age", "3600");

        if (httpRequest.getMethod().equals("OPTIONS")) {
            logger.info("OPTIONS");
            httpResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
            httpResponse.setHeader("Access-Control-Allow-Headers", "*");
//            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
//            httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");

            chain.doFilter(request, response);
            return;
        }
        String idToken = httpRequest.getHeader("uid");
        if (idToken == null) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        try {
            var route = httpRequest.getRequestURL().toString();
            UserRecord userRecord = FirebaseAuth.getInstance(FirebaseApp.getInstance("gromed-3c731")).getUser(idToken);
            UserContextHolder.setUtilisateur(utilisateurService.syncUser(userRecord,route));
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
