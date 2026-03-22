package com.bibliotech.bibliotechproject.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password; // Will be encoded
    private Set<String> roles; // e.g., "ROLE_ADMIN", "ROLE_USER"

    public User(String id, String username, String password, Set<String> roles) {

    }
}