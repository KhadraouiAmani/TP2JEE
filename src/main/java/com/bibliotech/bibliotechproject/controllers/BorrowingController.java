package com.bibliotech.bibliotechproject.controllers;

import com.bibliotech.bibliotechproject.dtos.BorrowingRequest;
import com.bibliotech.bibliotechproject.models.Borrowing;
import com.bibliotech.bibliotechproject.services.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/borrowings")
public class BorrowingController {

    @Autowired private BorrowingService borrowingService;

    @PostMapping("/checkout")
    public Mono<Borrowing> checkout(@RequestBody BorrowingRequest request) {
        return borrowingService.processBorrowing(request.getBookId(), request.getUserId());
    }
    @PostMapping("/return/{id}")
    public Mono<Void> returnBook(@PathVariable String id) {
        return borrowingService.returnBook(id);
    }

    @GetMapping("/user/{userId}")
    public Flux<Borrowing> getActive(@PathVariable String userId) {
        return borrowingService.getActiveBorrowings(userId);
    }
}