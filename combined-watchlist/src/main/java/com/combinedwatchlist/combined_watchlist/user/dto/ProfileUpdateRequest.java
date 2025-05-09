package com.combinedwatchlist.combined_watchlist.user.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ProfileUpdateRequest(
        @Nullable
        @Email
        @Size(max = 255, message = "Email can't be longer than 255 characters")
        String email,

        @Nullable
        @Size(max = 255, message = "Password can't be longer than 255 characters")
        String password
) {}
