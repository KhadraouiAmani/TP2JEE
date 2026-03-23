package com.bibliotech.bibliotechproject.services;

import com.bibliotech.bibliotechproject.models.Borrowing;
import com.bibliotech.bibliotechproject.models.BorrowingStatus;
import com.bibliotech.bibliotechproject.repositories.BookRepository;
import com.bibliotech.bibliotechproject.repositories.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class BorrowingService {

    @Autowired private BorrowingRepository borrowingRepository;
    @Autowired private BookRepository bookRepository;

    @Transactional // Requirement: Rule of Atomicity (Save both or neither)
    public Mono<Borrowing> processBorrowing(String bookId, String userId) {

        // 1. Find the book using the bookId from the request
        return bookRepository.findById(bookId)
                .switchIfEmpty(Mono.error(new RuntimeException("Book not found!")))
                .flatMap(book -> {

                    // RULE 1: Check stockDisponible
                    if (book.getStockDisponible() <= 0) {
                        return Mono.error(new RuntimeException("Stock is empty!"));
                    }

                    // RULE 2: Check if user has < 3 active borrowings
                    return borrowingRepository.findByUserIdAndStatus(userId, BorrowingStatus.ONGOING)
                            .count()
                            .flatMap(count -> {
                                if (count >= 3) {
                                    return Mono.error(new RuntimeException("User limit reached (3 books max)"));
                                }

                                // EVERYTHING OK -> Update the data
                                // Step 1: Decrease the stock
                                book.setStockDisponible(book.getStockDisponible() - 1);

                                // Step 2: Save the updated book, then save the borrowing
                                return bookRepository.save(book)
                                        .then(createNewBorrowingRecord(bookId, userId));
                            });
                });
    }

    private Mono<Borrowing> createNewBorrowingRecord(String bookId, String userId) {
        Borrowing b = new Borrowing();
        b.setBookId(bookId);
        b.setUserId(userId);
        b.setStatus(BorrowingStatus.ONGOING);
        b.setReturnDate(LocalDate.now().plusDays(14)); // Due in 2 weeks
        return borrowingRepository.save(b);
    }


    // Logic to RETURN a book
    public Mono<Void> returnBook(String borrowingId) {
        return borrowingRepository.findById(borrowingId)
                .flatMap(borrowing -> {
                    // 1. Change status to RETURNED
                    borrowing.setStatus(BorrowingStatus.RETURNED);
                    borrowing.setReturnDate(LocalDate.now()); // Actual return date

                    return borrowingRepository.save(borrowing)
                            .flatMap(b -> bookRepository.findById(b.getBookId()))
                            .flatMap(book -> {
                                // 2. Put the book back in stock!
                                book.setStockDisponible(book.getStockDisponible() + 1);
                                return bookRepository.save(book);
                            });
                }).then();
    }

    // Logic to LIST active borrowings
    public Flux<Borrowing> getActiveBorrowings(String userId) {
        return borrowingRepository.findByUserIdAndStatus(userId, BorrowingStatus.ONGOING);
    }

}