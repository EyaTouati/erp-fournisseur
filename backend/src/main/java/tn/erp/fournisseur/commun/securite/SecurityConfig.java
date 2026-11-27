package tn.erp.fournisseur.commun.securite;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableMethodSecurity // active @PreAuthorize
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filtre(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // cookie XSRF-TOKEN
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers("/api/auth/login"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/csrf").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .logout(l -> l
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT)));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService utilisateurs(PasswordEncoder encodeur) {
        String mdp = encodeur.encode("1234");
        return new InMemoryUserDetailsManager(
                User.withUsername("magasinier").password(mdp).roles("MAGASINIER").build(),
                User.withUsername("achats").password(mdp).roles("ACHATS").build(),
                User.withUsername("achats2").password(mdp).roles("ACHATS").build(),
                User.withUsername("directeur").password(mdp).roles("DIRECTEUR").build(),
                User.withUsername("comptable").password(mdp).roles("COMPTABLE").build());
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService uds, PasswordEncoder encodeur) {
        DaoAuthenticationProvider fournisseur = new DaoAuthenticationProvider(uds);
        fournisseur.setPasswordEncoder(encodeur);
        return new ProviderManager(fournisseur);
    }
}