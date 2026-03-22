package com.bibliotech.bibliotechproject.services;

import com.bibliotech.bibliotechproject.models.Book;
import com.bibliotech.bibliotechproject.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service // Dit à Spring : "C'est ici que se trouve la logique métier"
public class BookService {

    @Autowired // Spring va chercher le "Bibliothécaire" (Repository) tout seul
    private BookRepository bookRepository;

    // Récupérer tous les livres (Flux = plusieurs objets)
    public Flux<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Récupérer un livre par son ID
    public Mono<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    // Sauvegarder un livre (Mono = un seul objet)
    public Mono<Book> saveBook(Book book) {
        return bookRepository.save(book);
    }

    // Chercher par ISBN (la méthode spéciale que tu as créée dans le Repository)
    public Mono<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
}