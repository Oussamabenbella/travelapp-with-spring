package com.example.travelappv2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configuration CSRF (désactivé pour l'API mais activé ailleurs)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/h2-console/**", "/direct-groq-chat", "/api/**"
                )
            )
            
            // Configuration des en-têtes
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            )
            
            // Configuration des autorisations de requêtes
            .authorizeHttpRequests(auth -> auth
                // Accès public
                .requestMatchers("/", "/register", "/login", "/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Chatbot et ressources statiques accessibles sans authentification
                .requestMatchers("/chatbot-*.html", "/*.html", "/css/**", "/js/**", "/images/**").permitAll()
                
                // Endpoint API du chatbot Groq direct (crucial pour que ça fonctionne)
                .requestMatchers("/direct-groq-chat").permitAll()
                
                // API documentation et endpoints de chatbot sans authentification
                .requestMatchers("/api/**").permitAll()
                
                // Destinations - Visualisation publique mais gestion admin uniquement
                .requestMatchers("/destinations").permitAll()
                .requestMatchers("/destinations/add", "/destinations/edit/**", "/destinations/delete/**").hasRole("ADMIN")
                
                // Réservations - Chaque utilisateur peut gérer ses propres réservations
                .requestMatchers("/reservations/my", "/reservations/book/**").hasAnyRole("USER", "ADMIN")
                
                // Gestion globale - Admin uniquement
                .requestMatchers("/admin/**", "/users/**").hasRole("ADMIN")
                .requestMatchers("/reservations/all", "/reservations/manage/**").hasRole("ADMIN")
                
                // Attractions et voyages - Accessible par les utilisateurs connectés
                .requestMatchers("/trips/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/attractions/**").hasAnyRole("USER", "ADMIN")
                
                // Toutes les autres requêtes nécessitent une authentification
                .anyRequest().authenticated()
            )
            
            // Configuration de la connexion par formulaire
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            
            // Configuration de la déconnexion
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
            
        
        return http.build();
    }
}
