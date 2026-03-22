package com.bibliotech.bibliotechproject.repositories;

import com.bibliotech.bibliotechproject.models.Borrowing;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowingRepository extends ReactiveMongoRepository<Borrowing, String> {
}