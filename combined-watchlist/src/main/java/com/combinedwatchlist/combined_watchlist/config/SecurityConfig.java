package com.combinedwatchlist.combined_watchlist.config;

import com.combinedwatchlist.combined_watchlist.user.User;
import com.combinedwatchlist.combined_watchlist.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@Profile("!test")
public class SecurityConfig {

    private final GuestUserFilter guestUserFilter;
    private final UserRepository userRepository;
    private final boolean reactMode;
    private final String frontendBaseUrl;

    public SecurityConfig(GuestUserFilter guestUserFilter, UserRepository userRepository, @Value("${app.react-mode}") boolean reactMode, @Value("${frontend.base-url}") String frontendBaseUrl) {
        this.guestUserFilter = guestUserFilter;
        this.userRepository = userRepository;
        this.reactMode = reactMode;
        this.frontendBaseUrl = frontendBaseUrl;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        // Forbid DELETE mappings for everyone other than ADMIN user
                        .requestMatchers(HttpMethod.DELETE, "/api/movies/**", "/api/shows/**", "/api/watchlist/**")
                        .hasRole("ADMIN")
                        // GET for all authenticated and PATCH only for roles USER and ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/users/me").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/me").hasRole("USER")
                        // PUT to /api/watchlist/{id} requires USER or ADMIN (DB-level updates, session based updates still allowed for PUT /api/watchlist)
                        .requestMatchers(HttpMethod.PUT, "/api/watchlist/*").hasAnyRole("ADMIN", "USER")
                        // Allow everything else
                        .anyRequest().permitAll()
            )
            .formLogin(form -> form
                    .loginProcessingUrl("/api/users/login")
                    .successHandler((request, response, authentication) -> {
                        response.setStatus(HttpServletResponse.SC_OK);
                        String username = authentication.getName();
                        User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new IllegalStateException("User not found after login"));

                        // Store userId in session for frontend to use
                        request.getSession().setAttribute("userId", user.getId());
                        response.setContentType("application/json");
                        response.getWriter().write("{\"message\": \"Login successful\"}");
                    })
                    .failureHandler((request, response, exception) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"error\": \"Invalid credentials\"}");
                    })
                    .permitAll()
            )
            .rememberMe(rm -> rm.tokenValiditySeconds(60 * 60 * 24 * 30)) // 30 days
            .logout(logout -> logout
                    .logoutUrl("/api/users/logout")
                    .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) //set to STATELESS after switch to JWT
            .addFilterBefore(guestUserFilter, UsernamePasswordAuthenticationFilter.class);

        if (reactMode) {
            http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/users/login", "/api/users/logout"));
        } else {
            http.csrf(csrf -> csrf.disable());
        }

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        if (!reactMode) return null;

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(frontendBaseUrl));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}