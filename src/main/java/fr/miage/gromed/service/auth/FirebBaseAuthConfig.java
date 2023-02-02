package fr.miage.gromed.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebBaseAuthConfig {


    @Autowired
    private AuthFilter authFilter;

        @Bean
        public FilterRegistrationBean<AuthFilter> firebaseAuthFilter() {
            FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
//            registrationBean.setFilter(new AuthFilter());
            registrationBean.setFilter(authFilter);
            registrationBean.setOrder(1);
            registrationBean.addUrlPatterns("/api/*");
            return registrationBean;
        }
    }


