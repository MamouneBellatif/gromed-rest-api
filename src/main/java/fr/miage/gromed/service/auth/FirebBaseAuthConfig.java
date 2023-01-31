package fr.miage.gromed.service.auth;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebBaseAuthConfig {

        @Bean
        public FilterRegistrationBean<AuthFilter> firebaseAuthFilter() {
            FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new AuthFilter());
            registrationBean.addUrlPatterns("/api/*");
            return registrationBean;
        }
    }


