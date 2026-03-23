package com.bibliotech.bibliotechproject.repositories;

import com.bibliotech.bibliotechproject.models.Borrowing;
import com.bibliotech.bibliotechproject.models.BorrowingStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BorrowingRepository extends ReactiveMongoRepository<Borrowing, String> {

    // We need this to count how many "ONGOING" books a user has
    Flux<Borrowing> findByUserIdAndStatus(String userId, BorrowingStatus status);

}