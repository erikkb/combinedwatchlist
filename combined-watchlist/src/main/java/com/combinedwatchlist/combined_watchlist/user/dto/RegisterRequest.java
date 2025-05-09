package com.combinedwatchlist.combined_watchlist.user.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
        @Size(max = 255, message = "Username can't be longer than 255 characters")
        String username,
        @NotBlank
        @Size(max = 255, message = "Password can't be longer than 255 characters")
        String password,
        @Email
        @Nullable
        @Size(max = 255, message = "Email can't be longer than 255 characters")
        String email) {}

