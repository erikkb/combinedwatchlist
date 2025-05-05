package com.combinedwatchlist.combined_watchlist.user;

import com.combinedwatchlist.combined_watchlist.user.dto.PasswordResetRequest;
import com.combinedwatchlist.combined_watchlist.user.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request, HttpSession session) {
        try {
            userService.register(request, session);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RegistrationException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @ExceptionHandler(RegistrationException.class)
    public Map<String, String> handleRegistrationException(RegistrationException ex) {
        return Map.of("error", ex.getMessage());
    }

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "guestUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Guest users don't have a user profile");
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username);

        return Map.of(
                "userId", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole()
        );
    }

    @PatchMapping("/me")
    public void updateCurrentUser(@RequestBody Map<String, String> updates, Authentication authentication) {
        String username = authentication.getName();
        userService.updateUser(username, updates);
    }

    @DeleteMapping("/me")
    public void deleteCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        userService.deleteUser(username);
    }

    @PostMapping("/request-password-reset")
    @ResponseStatus(HttpStatus.OK)
    public void requestPasswordReset(@RequestBody PasswordResetRequest request) {
        userService.requestPasswordReset(request.email());
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        userService.resetPasswordWithToken(token, newPassword);
    }
}
