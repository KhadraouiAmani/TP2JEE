package com.bibliotech.bibliotechproject.models;

import lombok.AllArgsConstructor; // Add this
import lombok.NoArgsConstructor;  // Add this
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Set;

@Data
@Document(collection = "users")
@NoArgsConstructor  // Add this
@AllArgsConstructor // Add this
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private Set<String> roles;
}

