package com.combinedwatchlist.combined_watchlist.config;

import com.combinedwatchlist.combined_watchlist.user.GuestUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final GuestUserFilter guestUserFilter;

    public SecurityConfig(GuestUserFilter guestUserFilter) {
        this.guestUserFilter = guestUserFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        // Forbid DELETE mappings for guest users (this needs to be reworked, since all other users still can delete, only admin should be able to)
                        .requestMatchers(HttpMethod.DELETE, "/api/movies/**", "/api/shows/**", "/api/watchlist/**")
                        .not().hasRole("GUEST_USER")
                        // Allow everything else
                        .anyRequest().permitAll()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
            .addFilterBefore(guestUserFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}