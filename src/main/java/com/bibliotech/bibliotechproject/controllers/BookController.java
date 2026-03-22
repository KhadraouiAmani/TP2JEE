package com.bibliotech.bibliotechproject.controllers;

import com.bibliotech.bibliotechproject.models.Book;
import com.bibliotech.bibliotechproject.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController // Dit que c'est une API REST
@RequestMapping("/api/v1/books") // L'adresse URL de base
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping // Quand on fait un GET sur l'URL
    public Flux<Book> getAll() {
        return bookService.getAllBooks();
    }

    @PostMapping // Quand on veut ajouter un livre
    public Mono<Book> create(@RequestBody Book book) {
        return bookService.saveBook(book);
    }
}