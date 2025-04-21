package com.combinedwatchlist.combined_watchlist.config;

import com.combinedwatchlist.combined_watchlist.user.User;
import com.combinedwatchlist.combined_watchlist.user.UserRepository;
import com.combinedwatchlist.combined_watchlist.watchlist.Watchlist;
import com.combinedwatchlist.combined_watchlist.watchlist.WatchlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@Configuration
public class AdminUserInitializer {

    private static final Logger log = LoggerFactory.getLogger(AdminUserInitializer.class);

    @Bean
    public CommandLineRunner createAdminUser(
            UserRepository userRepository,
            WatchlistRepository watchlistRepository,
            PasswordEncoder passwordEncoder,
            @Value("${admin.username}") String adminUsername,
            @Value("${admin.password}") String adminPassword,
            @Value("${admin.email}") String adminEmail
    ) {
        return args -> {
            userRepository.findByUsername(adminUsername).ifPresentOrElse(
                user -> {
                    log.info("Admin user already exists.");
                    if (watchlistRepository.findByUserId(user.getId()).isEmpty()) {
                        Watchlist watchlist = new Watchlist();
                        watchlist.setUserId(user.getId());
                        watchlist.setMovieIds(new ArrayList<>());
                        watchlist.setShowIds(new ArrayList<>());
                        watchlistRepository.save(watchlist);
                        log.info("Watchlist created for existing admin user.");
                    }
                },
                () -> {
                    User admin = new User();
                    admin.setUsername(adminUsername);
                    admin.setPassword(passwordEncoder.encode(adminPassword));
                    admin.setEmail(adminEmail);
                    admin.setRole("ADMIN");

                    User savedAdmin = userRepository.save(admin);

                    Watchlist watchlist = new Watchlist();
                    watchlist.setUserId(savedAdmin.getId());
                    watchlist.setMovieIds(new ArrayList<>());
                    watchlist.setShowIds(new ArrayList<>());
                    watchlistRepository.save(watchlist);

                    log.info("Admin user and watchlist created.");
                }
            );
        };
    }
}
