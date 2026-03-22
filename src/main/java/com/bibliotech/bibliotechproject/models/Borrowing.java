package com.bibliotech.bibliotechproject.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Document(collection = "borrowings")
public class Borrowing {
    @Id
    private String id;
    private String bookId;
    private String userId;

    @CreatedDate // Se remplit tout seul grâce à notre config MongoConfig
    private LocalDate borrowDate;

    private LocalDate returnDate;

    // Utilise l'Enum créé au début (ONGOING, RETURNED, OVERDUE)
    private BorrowingStatus status;
}