package com.bibliotech.bibliotechproject.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "books")
public class Book {
    @Id
    private String id;

    @Indexed(unique = true) // Empêche d'avoir deux fois le même ISBN
    private String isbn;

    private String title;
    private int stockDisponible;

    // On stocke l'ID de l'auteur pour faire le lien (Relation Many-to-One)
    private String authorId;

    // On stocke une liste d'IDs de catégories (Relation Many-to-Many)
    private List<String> categoryIds;
}