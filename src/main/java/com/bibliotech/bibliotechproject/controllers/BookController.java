package com.bibliotech.bibliotechproject.controllers;

import com.bibliotech.bibliotechproject.models.Book;
import com.bibliotech.bibliotechproject.dtos.BookDTO;
import com.bibliotech.bibliotechproject.mappers.BookMapper;
import com.bibliotech.bibliotechproject.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/books") // L'adresse URL de base
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookMapper bookMapper; // Injects the "Translator"
    @GetMapping // Quand on fait un GET sur l'URL
    public Flux<BookDTO> getAll() {
        return bookService.getAllBooks().map(bookMapper::toDto); // Converts all books to DTOs;
    }
    @PostMapping // Pour ajouter un livre
    @ResponseStatus(HttpStatus.CREATED) // Forces "201 Created" status
    public Mono<BookDTO> create(@RequestBody Book book) {
        return bookService.saveBook(book)
                // added this part for the DTO translation
                .flatMap(savedBook -> {
                    BookDTO dto = bookMapper.toDto(savedBook);
                    return Mono.zip(
                            bookService.getAuthorName(savedBook.getAuthorId()),
                            bookService.getCategoryLabels(savedBook.getCategoryIds())
                    ).map(tuple -> {
                        dto.setAuthorName(tuple.getT1());
                        dto.setCategoryNames(tuple.getT2());
                        return dto;
                    });
                });
    }
    @GetMapping("/{id}")
    public Mono<BookDTO> getById(@PathVariable String id) {
        return bookService.getBookById(id)
                .flatMap(book -> {
                    BookDTO dto = bookMapper.toDto(book);
                    // On récupère les noms au lieu des IDs
                    return Mono.zip(
                            bookService.getAuthorName(book.getAuthorId()),
                            bookService.getCategoryLabels(book.getCategoryIds())
                    ).map(tuple -> {
                        dto.setAuthorName(tuple.getT1());
                        dto.setCategoryNames(tuple.getT2());
                        return dto;
                    });
                });
    }
    @GetMapping("/isbn/{isbn}")
    public Mono<BookDTO> getByIsbn(@PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn)
                .map(bookMapper::toDto);
    }
    //  Mettre à jour (PUT)
    @PutMapping("/{id}")
    public Mono<BookDTO> update(@PathVariable String id, @RequestBody Book book) {
        return bookService.updateBook(id, book)
                .map(bookMapper::toDto); // On renvoie le DTO mis à jour
    }

    //  Supprimer (DELETE)
    @DeleteMapping("/{id}")
    public Mono<String> delete(@PathVariable String id) {
        return bookService.deleteBook(id)
                .then(Mono.just("Livre supprimé avec succès !")); // On renvoie un message de confirmation
    }
}