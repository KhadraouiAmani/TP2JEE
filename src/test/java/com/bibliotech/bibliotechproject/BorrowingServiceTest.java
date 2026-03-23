package com.bibliotech.bibliotechproject;

import com.bibliotech.bibliotechproject.models.Book;
import com.bibliotech.bibliotechproject.models.Borrowing;
import com.bibliotech.bibliotechproject.repositories.BookRepository;
import com.bibliotech.bibliotechproject.repositories.BorrowingRepository;
import com.bibliotech.bibliotechproject.services.BorrowingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// 1. This tells JUnit to use Mockito for this test
@ExtendWith(MockitoExtension.class)
public class BorrowingServiceTest {

    // 2. Create "Fake" versions of the Repositories
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowingRepository borrowingRepository;

    // 3. Inject the fakes into your Service
    @InjectMocks
    private BorrowingService borrowingService;

    /**
     * PDF Requirement: Cas 1 - Succès de l'emprunt
     */
    @Test
    void testCheckout_Success() {
        // PREPARATION: Create a fake book with stock = 10
        Book fakeBook = new Book();
        fakeBook.setId("123");
        fakeBook.setStockDisponible(10);

        // BEHAVIOR: Tell the fake database what to do when we call it
        when(bookRepository.findById("123")).thenReturn(Mono.just(fakeBook));
        when(borrowingRepository.findByUserIdAndStatus(any(), any())).thenReturn(Flux.empty());
        when(bookRepository.save(any())).thenReturn(Mono.just(fakeBook));
        when(borrowingRepository.save(any())).thenReturn(Mono.just(new Borrowing()));

        // EXECUTION & ASSERTION: Use StepVerifier to watch the "Reactive" stream
        StepVerifier.create(borrowingService.processBorrowing("123", "user1"))
                .expectNextCount(1) // We expect 1 borrowing object back
                .verifyComplete();  // We expect it to finish without errors
    }

    /**
     * PDF Requirement: Cas 2 - Échec si le stock est épuisé
     */
    @Test
    void testCheckout_Fail_NoStock() {
        // PREPARATION: Create a fake book with stock = 0
        Book emptyBook = new Book();
        emptyBook.setId("456");
        emptyBook.setStockDisponible(0);

        // BEHAVIOR: If we look for book 456, return the empty book
        when(bookRepository.findById("456")).thenReturn(Mono.just(emptyBook));

        // EXECUTION & ASSERTION:
        StepVerifier.create(borrowingService.processBorrowing("456", "user1"))
                .expectErrorMessage("Stock is empty!") // We EXPECT this specific error
                .verify();
    }
}