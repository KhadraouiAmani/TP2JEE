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

@RestController // Dit que c'est une API REST
@RequestMapping("/api/v1/books") // L'adresse URL de base
public class BookController {

    @Autowired
    private BookService bookService;

    // --- LINE ADDED ---
    @Autowired
    private BookMapper bookMapper; // Injects your "Translator"
    // ------------------

    @GetMapping // Quand on fait un GET sur l'URL
    public Flux<BookDTO> getAll() {
        return bookService.getAllBooks().map(bookMapper::toDto); // Converts all books to DTOs;
    }

    @PostMapping // Quand on veut ajouter un livre
    // --- LINE ADDED ---
    @ResponseStatus(HttpStatus.CREATED) // Forces "201 Created" status
    // ------------------
    public Mono<BookDTO> create(@RequestBody Book book) {
        return bookService.saveBook(book)
                // added this part for the DTO translation (islem)
                .flatMap(savedBook -> {
                    // 1. Convert to DTO (authorName is null here)
                    BookDTO dto = bookMapper.toDto(savedBook);

                    // 2. Fetch the real names from Author and Category collections
                    return Mono.zip(
                            bookService.getAuthorName(savedBook.getAuthorId()),
                            bookService.getCategoryLabels(savedBook.getCategoryIds())
                    ).map(tuple -> {
                        dto.setAuthorName(tuple.getT1());   // Set the Name!
                        dto.setCategoryNames(tuple.getT2()); // Set the Names!
                        return dto;
                    });
                });
        //---
    }
}