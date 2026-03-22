package com.bibliotech.bibliotechproject.repositories;

import com.bibliotech.bibliotechproject.models.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    // Commande spéciale : "Cherche un livre par son numéro ISBN"
    // Ça renvoie un Mono (un ticket pour un livre)
    Mono<Book> findByIsbn(String isbn);
}