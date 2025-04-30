package com.combinedwatchlist.combined_watchlist.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import java.util.List;

@Configuration
@Profile("test")
public class TestSecurityHeaderConfig {

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.DELETE, "/api/movies/**", "/api/shows/**", "/api/watchlist/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/watchlist/*").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/users/me").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(testHeaderAuthenticationFilter(), AnonymousAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public Filter testHeaderAuthenticationFilter() {
        return (servletRequest, servletResponse, chain) -> {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String role = request.getHeader("X-Test-Role");

            if (role != null) {
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        "test-user",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            chain.doFilter(servletRequest, servletResponse);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
