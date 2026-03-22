package com.bibliotech.bibliotechproject.repositories;

import com.bibliotech.bibliotechproject.models.Author;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
    // Ici, on a toutes les méthodes de base : save(), delete(), findById()...
}