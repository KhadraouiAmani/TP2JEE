package com.bibliotech.bibliotechproject.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data // Génère automatiquement les Getters et Setters
@Document(collection = "authors") // Dit à MongoDB de créer une "table" authors
public class Author {
    @Id // Identifiant unique dans la base de données
    private String id;
    private String name;
    private String biography;
}