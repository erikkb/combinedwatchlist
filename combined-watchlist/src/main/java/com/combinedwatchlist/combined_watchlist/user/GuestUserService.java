package com.combinedwatchlist.combined_watchlist.user;

import org.springframework.stereotype.Service;

@Service
public class GuestUserService {

    private final GuestUserRepository guestUserRepository;

    public GuestUserService(GuestUserRepository guestUserRepository) {
        this.guestUserRepository = guestUserRepository;
    }

    public GuestUser getOrCreateGuestUser(String sessionId) {
        return guestUserRepository.findBySessionId(sessionId)
                .orElseGet(() -> {
                    // Spring Data JDBC will automatically generate an ID for the new GuestUser
                    GuestUser newGuestUser = new GuestUser(null, sessionId);
                    return guestUserRepository.save(newGuestUser);
                });
    }
}