package com.combinedwatchlist.combined_watchlist.config;

import com.combinedwatchlist.combined_watchlist.user.GuestUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;

@Component
public class GuestUserFilter extends OncePerRequestFilter {

    private final GuestUserService guestUserService;

    public GuestUserFilter(GuestUserService guestUserService) {
        this.guestUserService = guestUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();
        guestUserService.getOrCreateGuestUser(sessionId);

        // Assign ROLE_GUEST_USER to the current session if no authentication exists
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            var guestAuth = new UsernamePasswordAuthenticationToken(
                    "guestUser", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST_USER"))
            );
            SecurityContextHolder.getContext().setAuthentication(guestAuth);
        }

        filterChain.doFilter(request, response);
    }

}