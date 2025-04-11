package com.combinedwatchlist.combined_watchlist.config;

import com.combinedwatchlist.combined_watchlist.config.GuestUserFilter;
import com.combinedwatchlist.combined_watchlist.user.GuestUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class GuestUserFilterTest {

    @Test
    void shouldAssignGuestUserRoleWhenNoAuthenticationExists() throws Exception {
        // Mock the GuestUserService
        GuestUserService guestUserService = Mockito.mock(GuestUserService.class);
        GuestUserFilter filter = new GuestUserFilter(guestUserService);

        // Mock HttpServletRequest, HttpServletResponse, and HttpSession
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        // Mock session behavior
        Mockito.when(request.getSession()).thenReturn(session); // Default session
        Mockito.when(request.getSession(true)).thenReturn(session); // Create session if needed
        Mockito.when(session.getId()).thenReturn("mockSessionId");

        // Clear the SecurityContext
        SecurityContextHolder.clearContext();

        // Execute the filter
        filter.doFilterInternal(request, response, filterChain);

        // Verify the authentication is set with the correct role
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_GUEST_USER");
    }
}