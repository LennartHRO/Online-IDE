package edu.tum.ase.ui;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.*;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private final RequestForwarder requestForwarder;

    @Autowired
    public WebSecurityConfiguration(RequestForwarder requestForwarder) {
        this.requestForwarder = requestForwarder;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
        delegate.setCsrfRequestAttributeName("_csrf");
        CsrfTokenRequestHandler requestHandler = delegate::handle;

        return http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/**").authenticated();
                    auth.anyRequest().permitAll();
                })
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.invalidateHttpSession(true);
                    logout.clearAuthentication(true);
                    logout.deleteCookies("JSESSIONID");
                    logout.logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                        httpServletResponse.setStatus(HttpStatus.OK.value());
                    });
                })

                .csrf(csrf -> {
                    csrf.csrfTokenRepository(csrfTokenRepository());
                    csrf.csrfTokenRequestHandler(requestHandler);
                })
                .addFilterBefore((request, response, chain) -> {
                    HttpServletRequest httpRequest = (HttpServletRequest) request;
                    String requestUri = httpRequest.getRequestURI();

                    if (requestUri.contains("/api")) {
                        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                        // Check if the request is authenticated and contains the user's details
                        if (authentication != null && authentication.isAuthenticated()) {
                            String username = authentication.getName(); // Get the username from the Authentication object
                            try{
                            // Create HttpHeaders and add username as a header
                            requestForwarder.forward((HttpServletRequest) request,(HttpServletResponse) response, "http://localhost:8080"+requestUri, username);

                            // Process the response if needed
                        } catch (Exception e) {
                            e.printStackTrace();
                        }}

                    }
                    else {
                        chain.doFilter(request, response);
                    }
                }, SecurityContextPersistenceFilter.class)
                .build();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookiePath("/");
        return repository;
    }
}