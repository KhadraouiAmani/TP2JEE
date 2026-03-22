package com.bibliotech.bibliotechproject.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BorrowingRequest {
    @NotBlank(message = "Book ID is required")
    private String bookId;

    @NotBlank(message = "User ID is required")
    private String userId;
}