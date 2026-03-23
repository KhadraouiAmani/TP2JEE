package com.bibliotech.bibliotechproject.services;

import com.bibliotech.bibliotechproject.models.Book;
import com.bibliotech.bibliotechproject.repositories.AuthorRepository;
import com.bibliotech.bibliotechproject.repositories.BookRepository;
import com.bibliotech.bibliotechproject.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
@Service // Dit à Spring : "C'est ici que se trouve la logique métier"
public class BookService {
    @Autowired // Spring va chercher le "Bibliothécaire" (Repository) tout seul
    private BookRepository bookRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private CategoryRepository categoryRepository;

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
        //zidt hethi ala fazet duplique kali chat tb3itni khtr andi integration test
        // 1. Check if a book with this ISBN already exists
        return bookRepository.findByIsbn(book.getIsbn())
                .flatMap(existingBook -> {
                    // 2. If it exists, throw a "Custom Error"
                    return Mono.<Book>error(new RuntimeException("Duplicate Error: This ISBN is already in our library!"));
                })
                // 3. If it's empty, then save the new book
                .switchIfEmpty(bookRepository.save(book));
    }

    // Chercher par ISBN (la méthode spéciale que tu as créée dans le Repository)
    public Mono<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    // Helper: Finds an Author's name by ID
    public Mono<String> getAuthorName(String authorId) {
        if (authorId == null) return Mono.just("Unknown Author");
        return authorRepository.findById(authorId)
                .map(author -> author.getName())
                .defaultIfEmpty("Unknown Author");
    }

    // Helper: Finds Category labels by a list of IDs
    public Mono<List<String>> getCategoryLabels(List<String> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) return Mono.just(List.of());
        return categoryRepository.findAllById(categoryIds)
                .map(cat -> cat.getLabel())
                .collectList();
    }

    //  Supprimer un livre par son ID
    public Mono<Void> deleteBook(String id) {
        return bookRepository.deleteById(id);
    }

    //  Mettre à jour un livre existant
    public Mono<Book> updateBook(String id, Book bookDetails) {
        return bookRepository.findById(id) // On cherche d'abord si le livre existe
                .flatMap(existingBook -> {
                    // On met à jour les champs
                    existingBook.setTitle(bookDetails.getTitle());
                    existingBook.setIsbn(bookDetails.getIsbn());
                    existingBook.setStockDisponible(bookDetails.getStockDisponible());
                    return bookRepository.save(existingBook); // On sauvegarde les changements
                });
    }
}